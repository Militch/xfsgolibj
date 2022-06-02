package tech.xfs.xfsgolibj.jsonrpc;

public class ErrorData {
    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public ErrorData setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ErrorData setMessage(String message) {
        this.message = message;
        return this;
    }
}
