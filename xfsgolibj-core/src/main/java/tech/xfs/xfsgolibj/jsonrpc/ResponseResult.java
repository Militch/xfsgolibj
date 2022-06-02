package tech.xfs.xfsgolibj.jsonrpc;

import com.google.gson.JsonElement;

public class ResponseResult {
    private String jsonrpc;
    private Integer id;
    private JsonElement result;
    private ErrorData error;
    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setError(ErrorData error) {
        this.error = error;
    }

    public ErrorData getError() {
        return error;
    }

    public JsonElement getResult() {
        return result;
    }

    public void setResult(JsonElement result) {
        this.result = result;
    }
}
