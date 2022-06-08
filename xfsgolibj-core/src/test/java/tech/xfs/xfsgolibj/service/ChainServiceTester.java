package tech.xfs.xfsgolibj.service;

import tech.xfs.xfsgolibj.common.*;
import tech.xfs.xfsgolibj.core.RPCClient;
import tech.xfs.xfsgolibj.jsonrpc.HTTPRPCClient;
import tech.xfs.xfsgolibj.test.TestJSONRPCRequester;

import java.math.BigInteger;
import java.util.List;

public class ChainServiceTester {
    private final RPCChainService chainService;
    private final TestJSONRPCRequester requester = new TestJSONRPCRequester();

    public ChainServiceTester() {
        RPCClient client = new HTTPRPCClient(requester);
        this.chainService = new RPCChainService(client);

    }

    public Hash sendRawTransaction(String resp, RawTransaction rawTransaction) throws Exception {
        requester.setResponseMockListener(()->resp);
        return chainService.sendRawTransaction(rawTransaction);
    }

    public List<EventLog> getLogs(String resp, EventLogsRequest request) throws Exception {
        requester.setResponseMockListener(()->resp);
        return chainService.getLogs(request);
    }
    public byte[] vmCall(String resp, CallRequest request) throws Exception{
        requester.setResponseMockListener(()->resp);
        return chainService.vmCall(request);
    }
    public BlockHeader getHead(String resp) throws Exception {
        requester.setResponseMockListener(()->resp);
        return chainService.getHead();
    }
    public Long getAddressTxNonce(String resp, Address address) throws Exception {
        requester.setResponseMockListener(()->resp);
        return chainService.getAddressTxNonce(address);
    }
    public List<Transaction> getTransactionsByBlockHash(String resp, Hash blockHash) throws Exception {
        requester.setResponseMockListener(()->resp);
        return chainService.getTransactionsByBlockHash(blockHash);
    }
    public Transaction getTransactionByHash(String resp, Hash blockHash) throws Exception {
        requester.setResponseMockListener(()->resp);
        return chainService.getTransactionByHash(blockHash);
    }
    public TransactionReceipt getReceiptByHash(String resp, Hash transactionHash) throws Exception {
        requester.setResponseMockListener(()->resp);
        return chainService.getReceiptByHash(transactionHash);
    }
    public Account getAccount(String resp, AccountRequest request) throws Exception {
        requester.setResponseMockListener(()->resp);
        return chainService.getAccount(request);
    }
    public BigInteger getBalance(String resp, Address address) throws Exception {
        requester.setResponseMockListener(()->resp);
        return chainService.getBalance(address);
    }
    public byte[] getStorageAt(String resp, StorageAtRequest request) throws Exception {
        requester.setResponseMockListener(()->resp);
        return chainService.getStorageAt(request);
    }
    public List<Transaction> getPendingTransactions(String resp) throws Exception {
        requester.setResponseMockListener(()->resp);
        return chainService.getPendingTransactions();
    }
    public List<Transaction> getQueueTransactions(String resp) throws Exception {
        requester.setResponseMockListener(()->resp);
        return chainService.getQueueTransactions();
    }
    public Block getBlockByHash(String resp, Hash hash) throws Exception {
        requester.setResponseMockListener(()->resp);
        return chainService.getBlockByHash(hash);
    }
    public Block getBlockByNumber(String resp, Long number) throws Exception {
        requester.setResponseMockListener(()->resp);
        return chainService.getBlockByNumber(number);
    }
    public Hash getBlockHashByNumber(String resp, long number) throws Exception {
        requester.setResponseMockListener(()->resp);
        return chainService.getBlockHashByNumber(number);
    }
    public List<Hash> getBlockHashesByRange(String resp, long start, long end) throws Exception {
        requester.setResponseMockListener(()->resp);
        return chainService.getBlockHashesByRange(start, end);
    }
}
