package tech.xfs.xfsgolibj.test;

import com.google.common.reflect.TypeParameter;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.TypeAdapter;
import tech.xfs.xfsgolibj.common.*;
import tech.xfs.xfsgolibj.core.RPCClient;
import tech.xfs.xfsgolibj.jsonrpc.ErrorData;
import tech.xfs.xfsgolibj.jsonrpc.JSONRPCRequester;
import tech.xfs.xfsgolibj.jsonrpc.RPCRequestException;
import tech.xfs.xfsgolibj.jsonrpc.ResponseResult;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TestJSONRPCRequester implements JSONRPCRequester {
    private ResponseMockListener responseMockListener;

    public void setResponseMockListener(ResponseMockListener responseMockListener) {
        this.responseMockListener = responseMockListener;
    }

    @Override
    public String request(String requestBody) throws Exception {
        if(responseMockListener == null){
            return null;
        }
        return responseMockListener.jsonResponse();
    }
}
