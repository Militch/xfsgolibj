package tech.xfs.xfsgolibj.core;

import okhttp3.Response;

import java.io.IOException;
import java.util.List;

public interface RPCClient {
    <T,D> T call(String method, D data, Class<T> tclass) throws Exception;
    <T,D> List<T> callList(String method, D data) throws Exception;
    Response proxy(String body) throws IOException;
}
