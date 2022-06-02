package tech.xfs.xfsgolibj.service;

import com.google.gson.Gson;
import tech.xfs.xfsgolibj.common.CallRequest;
import tech.xfs.xfsgolibj.common.Address;
import tech.xfs.xfsgolibj.common.Hash;
import tech.xfs.xfsgolibj.common.Transaction;
import tech.xfs.xfsgolibj.core.ChainService;
import tech.xfs.xfsgolibj.core.RPCClient;
import tech.xfs.xfsgolibj.utils.Bytes;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

public class RPCChainService implements ChainService {
    private final RPCClient client;
    private static final Gson g = new Gson();
    public RPCChainService(RPCClient client){
        this.client = client;
    }
    @Override
    public void sendRawTransaction(Transaction transaction) throws Exception {
        if (transaction == null){
            throw new IllegalArgumentException("transaction is null");
        }
        Map<String,String> map = transaction.getSerializeMap(false);
        String jsonString = g.toJson(map);
        Base64.Encoder encoder = Base64.getEncoder();
        String rawEncoded = encoder.encodeToString(jsonString.getBytes(StandardCharsets.UTF_8));
        String data = client.call("TxPool.SendRawTransaction",
                new String[]{rawEncoded}, String.class);
        Hash transactionHash = Hash.fromHex(data);
        if (!transactionHash.equals(transaction.getHash())){
            throw new Exception(String.format("hash not match: got: %s, want: %s",transactionHash, transaction.getHash().toHexString()));
        }
    }
    @Override
    public byte[] call(CallRequest request) throws Exception {
        if (request == null){
            throw new IllegalArgumentException("request is null");
        }
        String[] params = new String[]{
                request.getStateRoot()==null?null:request.getStateRoot().toHexString(),
                request.getFrom()==null?null:request.getFrom().toBase58(),
                request.getTo()==null?null:request.getTo().toBase58(),
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
        return client.call("TxPool.GetAddrTxNonce",
                new String[]{address.toBase58()}, Long.class);
    }
    public void getHead(){}
    public void getLogs(){}
    public void getBlockByNumber(){}
    public void getBlockByHash(){}
    public void getTransaction(){}
    public void getAccount(){}
    public void getBalance(){}
}
