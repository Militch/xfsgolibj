package tech.xfs.xfsgolibj.jsonrpc;

import com.google.gson.Gson;
import okhttp3.*;

import java.io.IOException;

public class OKHTTPRequester implements JSONRPCRequester {
    private static final String JSON_RPC_VERSION = "2.0";
    private final String apiurl;
    public static final MediaType MEDIA_TYPE_JSON
            = MediaType.get("application/json; charset=utf-8");
    private static final OkHttpClient okc = new OkHttpClient.Builder().build();

    public OKHTTPRequester(String apiurl) {
        this.apiurl = apiurl;
    }

    @Override
    public String request(String requestBody) throws Exception {
        RequestBody body = RequestBody.create(requestBody, MEDIA_TYPE_JSON);
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
        }
    }
}
