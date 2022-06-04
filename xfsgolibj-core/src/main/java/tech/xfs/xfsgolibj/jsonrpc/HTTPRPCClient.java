package tech.xfs.xfsgolibj.jsonrpc;

import com.google.common.reflect.TypeParameter;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.utils.Strings;
import tech.xfs.xfsgolibj.core.RPCClient;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HTTPRPCClient implements RPCClient {
    private static final Logger log = LoggerFactory.getLogger(HTTPRPCClient.class);
    private static final OkHttpClient okc = new OkHttpClient.Builder().build();
    public static final MediaType MEDIA_TYPE_JSON
            = MediaType.get("application/json; charset=utf-8");
    private static final String JSON_RPC_VERSION = "2.0";
    private static final Gson g = new Gson().newBuilder().serializeNulls().create();
    private final String apiurl;

    public HTTPRPCClient(String apiurl){
        this.apiurl = apiurl;
    }

    private ResponseResult parseResult(String body) throws RPCRequestException {
        ResponseResult responseResult = g.fromJson(body, ResponseResult.class);
        ErrorData errorData = responseResult.getError();
        if (errorData != null){
            throw new RPCRequestException(errorData.getCode(), errorData.getMessage());
        }
        return responseResult;
    }
    @Override
    public <T, D> T call(String method, D data, Class<T> tclass) throws Exception {
        String body = request(method, data);
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
    private <D> String request(String method, D data) throws Exception{
        RequestParams<D> params = new RequestParams<>();
        params.setId(1);
        params.setJsonrpc(JSON_RPC_VERSION);
        params.setMethod(method);
        params.setParams(data);
        String jsonParams = g.toJson(params);
        RequestBody body = RequestBody.create(jsonParams, MEDIA_TYPE_JSON);
        Request request = new Request.Builder()
                .url(apiurl)
                .post(body)
                .build();
        try (Response response = okc.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            ResponseBody rb = response.body();
            if (rb == null){
                throw new NullPointerException("response.body == null");
            }
            return rb.string();
        }catch (Exception e) {
            log.debug("RPC Request failed: {}", e.getMessage());
            throw e;
        }
    }
    private static <T> Type setModelAndGetCorrespondingList(Class<T> tClass){
        return new TypeToken<ArrayList<T>>(){}.where(new TypeParameter<T>() { },tClass).getType();
    }
    @Override
    public <T, D> List<T> callList(String method, D data, Class<T> tClass) throws Exception {
        String body = request(method, data);
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

    @Override
    public Response proxy(String body) throws IOException {
        RequestBody rqBody = RequestBody.create(body, MEDIA_TYPE_JSON);
        Request request = new Request.Builder()
                .url(apiurl)
                .post(rqBody)
                .build();
        return okc.newCall(request).execute();
    }
}
