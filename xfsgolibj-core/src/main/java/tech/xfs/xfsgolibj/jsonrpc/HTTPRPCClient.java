package tech.xfs.xfsgolibj.jsonrpc;

import com.google.common.reflect.TypeParameter;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.TypeAdapter;
import org.web3j.utils.Strings;
import tech.xfs.xfsgolibj.common.*;
import tech.xfs.xfsgolibj.common.Address;
import tech.xfs.xfsgolibj.core.RPCClient;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HTTPRPCClient implements RPCClient {
    private static final String JSON_RPC_VERSION = "2.0";
    private static final TypeAdapter<Hash> hashAdapter;
    private static final TypeAdapter<Address> addressAdapter;
    private static final TypeAdapter<byte[]> bytesAdapter;
    static {
        hashAdapter = new HashAdapter();
        addressAdapter = new AddressAdapter();
        bytesAdapter = new BytesAdapter();
    }
    private final static Gson g = new Gson().newBuilder()
            .serializeNulls()
            .registerTypeAdapter(Hash.class, hashAdapter)
            .registerTypeAdapter(Address.class, addressAdapter)
            .registerTypeAdapter(byte[].class, bytesAdapter)
            .create();
    private final JSONRPCRequester requester;

    public HTTPRPCClient(JSONRPCRequester requester){
        this.requester = requester;
    }
    public HTTPRPCClient(String apiurl){
        this.requester = new OKHTTPRequester(apiurl);
    }
    private ResponseResult parseResult(String body) throws RPCRequestException {
        ResponseResult responseResult = g.fromJson(body, ResponseResult.class);
        ErrorData errorData = responseResult.getError();
        if (errorData != null){
            throw new RPCRequestException(errorData.getCode(), errorData.getMessage());
        }
        return responseResult;
    }
    private List<ResponseResult> parseResultBatch(String body) throws RPCRequestException {
        return g.fromJson(body, new TypeToken<List<ResponseResult>>(){}.getType());
    }
    private RequestParams requestPack(String method, Object data, int reqId){
        RequestParams params = new RequestParams();
        params.setId(reqId);
        params.setJsonrpc(JSON_RPC_VERSION);
        params.setMethod(method);
        params.setParams(data);
        return params;
    }
    @Override
    public <T> T call(String method, Object data, Class<T> tclass) throws Exception {
        RequestParams params = requestPack(method, data, 1);
        String requestJsonString = g.toJson(params);
        String body = requester.request(requestJsonString);
        if (Strings.isEmpty(body)) {
            throw new Exception("response.body.string is empty");
        }
        ResponseResult responseResult = parseResult(body);
        JsonElement result = responseResult.getResult();
        if (result == null || result.isJsonNull()){
            return null;
        }
        return g.fromJson(result, tclass);
    }

    @Override
    public <T,D> List<T> callBatch(String method, List<D[]> data, Class<T> tClass) throws Exception {
        return callBatch(method, data, tClass, false);
    }

    @Override
    public <T,D> List<T> callBatch(String method, List<D[]> data, Class<T> tClass, boolean ignoreError) throws Exception {
        List<RequestParams> batches = new ArrayList<>();
        for (int i=0; i<data.size(); i++){
            RequestParams params = requestPack(method, data, i);
            batches.add(params);
        }
        String requestJsonString = g.toJson(batches);
        String body = requester.request(requestJsonString);
        if (Strings.isEmpty(body)){
            throw new Exception("response.body.string is empty");
        }
        List<ResponseResult> responseResult = parseResultBatch(body);
        List<T> out = new ArrayList<>();
        for (ResponseResult result : responseResult){
            ErrorData errorData = result.getError();
            JsonElement resultElement = result.getResult();
            if (errorData != null && ignoreError){
                continue;
            }else if (errorData != null){
                throw new RPCRequestException(errorData.getCode(), String.format("%s, from batch request", errorData.getMessage()));
            }
            if (resultElement == null || resultElement.isJsonNull()){
                out.add(null);
            }
            T t = g.fromJson(resultElement, tClass);
            out.add(t);
        }
        return out;
    }

    private static <T> Type setModelAndGetCorrespondingList(Class<T> tClass){
        return new TypeToken<ArrayList<T>>(){}.where(new TypeParameter<T>() { },tClass).getType();
    }
    @Override
    public <T> List<T> callList(String method, Object data, Class<T> tClass) throws Exception {
        RequestParams params = requestPack(method, data, 1);
        String requestBody = g.toJson(params);
        String body = requester.request(requestBody);
        if (Strings.isEmpty(body)){
            throw new NullPointerException("body is null");
        }
        ResponseResult responseResult = parseResult(body);
        JsonElement result = responseResult.getResult();
        if (result == null || result.isJsonNull()){
            return null;
        }
        Type t = setModelAndGetCorrespondingList(tClass);
        return g.fromJson(result, t);
    }

}
