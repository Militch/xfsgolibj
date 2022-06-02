package tech.xfs.xfsgolibj.core;


import tech.xfs.xfsgolibj.common.CallRequest;
import tech.xfs.xfsgolibj.common.Address;
import tech.xfs.xfsgolibj.common.Transaction;

public interface ChainService {
    void sendRawTransaction(Transaction transaction) throws Exception;
    Long getAddressTxNonce(Address address) throws Exception;
    byte[] call(CallRequest request) throws Exception;
    void getHead() throws Exception;
}
