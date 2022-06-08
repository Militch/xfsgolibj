package tech.xfs.xfsgolibj.core;

import okhttp3.Response;

import java.io.IOException;
import java.util.List;

public interface RPCClient {
    <T> T call(String method, Object data, Class<T> tclass) throws Exception;
    <T,D> List<T> callBatch(String method, List<D[]> data, Class<T> tClass) throws Exception;
    <T,D> List<T> callBatch(String method, List<D[]> data, Class<T> tClass, boolean ignoreError) throws Exception;
    <T> List<T> callList(String method, Object data, Class<T> tClass) throws Exception;
}
