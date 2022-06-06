package tech.xfs.xfsgolibj.jsonrpc;

import com.google.common.reflect.TypeParameter;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.TypeAdapter;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.utils.Strings;
import tech.xfs.xfsgolibj.common.*;
import tech.xfs.xfsgolibj.common.Address;
import tech.xfs.xfsgolibj.core.RPCClient;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
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
    private <T> String requestPack(String method, T data){
        RequestParams<T> params = new RequestParams<>();
        params.setId(0);
        params.setJsonrpc(JSON_RPC_VERSION);
        params.setMethod(method);
        params.setParams(data);
        return g.toJson(params);
    }
    @Override
    public <T, D> T call(String method, D data, Class<T> tclass) throws Exception {
        String requestJsonString = requestPack(method, data);
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
    private static <T> Type setModelAndGetCorrespondingList(Class<T> tClass){
        return new TypeToken<ArrayList<T>>(){}.where(new TypeParameter<T>() { },tClass).getType();
    }
    @Override
    public <T, D> List<T> callList(String method, D data, Class<T> tClass) throws Exception {
        String requestBody = requestPack(method, data);
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
