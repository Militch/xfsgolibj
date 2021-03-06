package tech.xfs.xfsgolibj.service;

import com.google.gson.Gson;
import tech.xfs.xfsgolibj.common.*;
import tech.xfs.xfsgolibj.core.ChainService;
import tech.xfs.xfsgolibj.core.RPCClient;
import tech.xfs.xfsgolibj.utils.Bytes;
import tech.xfs.xfsgolibj.utils.Strings;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class RPCChainService implements ChainService {
    private final RPCClient client;
    private static final Gson g = new Gson();
    public RPCChainService(RPCClient client){
        this.client = client;
    }
    @Override
    public Hash sendRawTransaction(RawTransaction transaction) throws Exception {
        if (transaction == null){
            throw new IllegalArgumentException("transaction is null");
        }
        Map<String,String> map = transaction.getSerializeMap(false);
        String jsonString = g.toJson(map);
        Base64.Encoder encoder = Base64.getEncoder();
        String rawEncoded = encoder.encodeToString(jsonString.getBytes(StandardCharsets.UTF_8));
        return client.call("TxPool.SendRawTransaction",
                new String[]{rawEncoded}, Hash.class);
    }
    @Override
    public byte[] vmCall(CallRequest request) throws Exception {
        if (request == null){
            throw new IllegalArgumentException("request is null");
        }
        String[] params = new String[]{
                Strings.valueOf(request.getStateRoot()),
                Strings.valueOf(request.getFrom()),
                Strings.valueOf(request.getTo()),
                Bytes.toHexString(request.getData()),
        };
        String hex = client.call("VM.Call",params,String.class);
        return Bytes.hexToBytes(hex);
    }
    @Override
    public Long getAddressTxNonce(Address address) throws Exception {
        if (address == null){
            throw new IllegalArgumentException("address is null");
        }
        return client.call("TxPool.GetAddrTxNonce", new String[]{
                Strings.valueOf(address)
        }, Long.class);
    }
    public BlockHeader getHead() throws Exception {
        return client.call("Chain.GetHead", null,BlockHeader.class);
    }

    @Override
    public BlockHeader getBlockHeaderByNumber(long number) throws Exception {
        return client.call("Chain.GetBlockHeaderByNumber", new String[]{
                Strings.valueOf(number)
        }, BlockHeader.class);
    }

    @Override
    public BlockHeader getBlockHeaderByHash(Hash hash) throws Exception {
        if(hash == null){
            throw new IllegalArgumentException("hash is null");
        }
        return client.call("Chain.GetBlockHeaderByHash", new String[]{
                Strings.valueOf(hash)
        }, BlockHeader.class);
    }

    @Override
    public List<Transaction> getTransactionsByBlockHash(Hash blockHash) throws Exception {
        if (blockHash == null){
            throw new IllegalArgumentException("block hash is empty");
        }
        return client.callList("Chain.GetTxsByBlockHash", new String[]{
                Strings.valueOf(blockHash),
        }, Transaction.class);
    }

    @Override
    public Transaction getTransactionByHash(Hash transactionHash) throws Exception {
        if (transactionHash == null){
            throw new IllegalArgumentException("transaction_hash is empty");
        }
        return client.call("Chain.GetTransaction", new String[]{
                Strings.valueOf(transactionHash)
        }, Transaction.class);
    }

    @Override
    public TransactionReceipt getReceiptByHash(Hash transactionHash) throws Exception {
        if (transactionHash == null){
            throw new IllegalArgumentException("transaction_hash is empty");
        }
        return client.call("Chain.GetReceiptByHash", new String[]{
                Strings.valueOf(transactionHash),
        }, TransactionReceipt.class);
    }

    @Override
    public List<EventLog> getLogs(EventLogsRequest logsRequest) throws Exception {
        if (logsRequest == null){
            throw new IllegalArgumentException("transaction_hash is empty");
        }
        return client.callList("Chain.GetLogs", new String[]{
                Strings.valueOf(logsRequest.getFromBlock()),
                Strings.valueOf(logsRequest.getToBlock()),
                Strings.valueOf(logsRequest.getAddress()),
                Strings.valueOf(logsRequest.getEventHash()),
        }, EventLog.class);
    }

    @Override
    public Account getAccount(AccountRequest request) throws Exception {
        if (request == null){
            throw new IllegalArgumentException("request is null");
        }
        return client.call("State.GetAccount", new String[]{
                Strings.valueOf(request.getStateRoot()),
                Strings.valueOf(request.getAddress()),
        }, Account.class);
    }

    @Override
    public BigInteger getBalance(Address address) throws Exception {
        if (address == null){
            throw new IllegalArgumentException("address is null");
        }
        return client.call("State.GetBalance", new String[]{
                Strings.valueOf(address),
        },BigInteger.class);
    }

    @Override
    public byte[] getStorageAt(StorageAtRequest request) throws Exception {
        if (request == null){
            throw new IllegalArgumentException("request is null");
        }
        return client.call("State.GetStorageAt", new String[]{
                Strings.valueOf(request.getStateRoot()),
                Strings.valueOf(request.getAddress()),
                Strings.valueOf(request.getKey()),
        }, byte[].class);
    }

    @Override
    public List<Transaction> getPendingTransactions() throws Exception {
        return client.callList("TxPool.GetPending", null, Transaction.class);
    }

    @Override
    public List<Transaction> getQueueTransactions() throws Exception {
        return client.callList("TxPool.GetQueue", null, Transaction.class);
    }

    @Override
    public Block getBlockByHash(Hash hash) throws Exception {
        if(hash == null){
            throw new IllegalArgumentException("block hash");
        }
        return client.call("Chain.GetBlockByHash",new String[]{
                Strings.valueOf(hash),
        },Block.class);
    }

    @Override
    public Block getBlockByNumber(Long number) throws Exception {
        if (number == null){
            throw new IllegalArgumentException("block number");
        }
        return client.call("Chain.GetBlockByNumber", new String[]{
                Strings.valueOf(number),
        },Block.class);
    }

    @Override
    public Hash getBlockHashByNumber(long number) throws Exception {
        if (number < 0){
            throw new IllegalArgumentException("number is < 0");
        }
        return client.call("Chain.GetBlockHashByNumber", new String[]{String.valueOf(number)},Hash.class);
    }

    @Override
    public List<Hash> getBlockHashesByRange(long start, long end) throws Exception {
        if (start < 0 || end < 0){
            throw new IllegalArgumentException("range is < 0");
        }
        long rangeMin = Math.min(start, end);
        long rangeMax = Math.max(start, end);
        List<String[]> request = new ArrayList<>();
        for (long i=rangeMin; i<rangeMax; i++){
            request.add(new String[]{String.valueOf(i)});
        }
        if (rangeMin == rangeMax){
            request.add(new String[]{String.valueOf(rangeMin)});
        }
        List<Hash> all = client.callBatch("Chain.GetBlockHashByNumber", request, Hash.class);
        if (all == null){
            return null;
        }
        return all.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    @Override
    public List<Block> getBlocksByBatch(List<Hash> hashes) throws Exception {
        if (hashes == null || hashes.size() == 0){
            throw new IllegalArgumentException("hashes is empty");
        }
        List<String[]> request = new ArrayList<>();
        for (Hash hash : hashes) {
            request.add(new String[]{String.valueOf(hash)});
        }
        List<Block> all = client.callBatch("Chain.GetBlockByHash", request, Block.class);
        if (all == null){
            return null;
        }
        return all.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    @Override
    public List<TransactionReceipt> getReceiptsByBlock(Block block) throws Exception {
        if (block == null){
            throw new IllegalArgumentException("block is null");
        }
        List<Transaction> txs = block.getTransactions();
        if (txs == null || txs.size() == 0){
            return null;
        }
        List<String[]> request = new ArrayList<>();
        for (Transaction tx : txs) {
            request.add(new String[]{
                    String.valueOf(tx.getHash())
            });
        }
        List<TransactionReceipt> all = client.callBatch("Chain.GetReceiptByHash",
                request, TransactionReceipt.class);
        if (all == null){
            return null;
        }
        return all.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }
}
