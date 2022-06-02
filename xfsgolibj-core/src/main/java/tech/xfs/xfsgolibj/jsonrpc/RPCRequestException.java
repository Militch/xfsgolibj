package tech.xfs.xfsgolibj.jsonrpc;

public class RPCRequestException extends Exception{
    private int code;
    private String message;

    public RPCRequestException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
    public int getCode(){
        return code;
    }

    @Override
    public String toString() {
        return String.format("RPC Request Error: code=%d, message=%s", code,
                message.replace("\n","").replace("\r",""));
    }
}
