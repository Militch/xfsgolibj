package tech.xfs.xfsgolibj.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.xfs.xfsgolibj.common.*;
import tech.xfs.xfsgolibj.service.RPCChainService;
import tech.xfs.xfsgolibj.utils.AddressUtil;
import tech.xfs.xfsgolibj.utils.Bytes;

import java.math.BigDecimal;
import java.math.BigInteger;

public abstract class Contract<T extends Contract.Caller, D extends Contract.Sender> {
    private static final Logger logger = LoggerFactory.getLogger(Contract.class);
    private static final BigDecimal DEFAULT_GAS_PRICE = BigDecimal.valueOf(10);
    private static final Long DEFAULT_GAS_LIMIT = 25000L;
    private final BINs bins;
    private final ABIExport abiExport;
    private final ChainService service;
    public static final class CallOpts {
        private Address from;
        private Long blockNumber;

    }
    public static class Caller{
        final Contract<?,?> contract;
        final Address address;
        public Caller(Contract<?,?> contract, Address address) {
            this.contract = contract;
            this.address = address;
        }
        protected Object call(CallOpts opts, String method, Object... params) throws Exception {
            return this.contract.call(opts, method, address, params);
        }
    }
    public static class Sender{
        final Contract<?,?> contract;

        final Address address;
        public Sender(Contract<?,?> contract, Address address) {
            this.contract = contract;
            this.address = address;
        }
        protected Hash send(TransactionOpts opts, String method, Object... params) throws Exception {
            return this.contract.send(opts, method, address, params);
        }
    }
    public abstract static class Event {
        final Contract<?,?> contract;
        final Tuple2<String, ABIExport.EventABIObj> eventABIObjPair;
        public Event(Contract<?,?> contract, String eventName) {
            this.contract = contract;
            eventABIObjPair = this.contract.findEventByName(eventName);
        }
        public String getHash(){
            return eventABIObjPair.getFirst();
        }
        protected abstract void parse(byte[] data);
    }
    public Contract(RPCClient client, BINs bins, ABIExport abiExport){
        this.bins = bins;
        this.abiExport = abiExport;
        this.service = new RPCChainService(client);
    }
    private Long getNonce(TransactionOpts opts) throws Exception {
        return service.getAddressTxNonce(opts.getFrom());
    }
    private Transaction createStdTransaction(TransactionOpts opts, Address contractAddress, byte[] data) throws Exception {
        BigInteger value = opts.getValue();
        if (value == null){
            value = BigInteger.ZERO;
        }
        BigDecimal gasPrice = opts.getGasPrice();
        if (gasPrice == null){
            gasPrice = DEFAULT_GAS_PRICE;
        }
        Long gasLimit = opts.getGasLimit();
        if (gasLimit == null){
            gasLimit = DEFAULT_GAS_LIMIT;
        }
        Long nonce = opts.getNonce();
        if (nonce == null){
            nonce = getNonce(opts);
        }
        BigInteger gasPriceAtto = Coin.nanoToAtto(gasPrice);
        Transaction stdtx = new Transaction();
        stdtx.setVersion(1);
        stdtx.setValue(value);
        stdtx.setGasPrice(gasPriceAtto);
        stdtx.setGasLimit(gasLimit);
        stdtx.setNonce(nonce);
        stdtx.setTo(contractAddress);
        stdtx.setData(data);
        return stdtx;
    }
    private Transaction transact(TransactionOpts opts, Address contractAddress, byte[] data) throws Exception {
        Transaction tx = createStdTransaction(opts, contractAddress, data);
        Signer signer = opts.getSigner();
        if (signer == null){
            throw new Exception("Signer is null");
        }
        Transaction signedTx = signer.sign(opts.getFrom(),tx);
        service.sendRawTransaction(signedTx);
        return signedTx;
    }
    public static final class DeployResult {
        private Hash transactionHash;
        private Address contractAddress;

        public Hash getTransactionHash() {
            return transactionHash;
        }

        public void setTransactionHash(Hash transactionHash) {
            this.transactionHash = transactionHash;
        }

        public Address getContractAddress() {
            return contractAddress;
        }

        public void setContractAddress(Address contractAddress) {
            this.contractAddress = contractAddress;
        }
    }
    protected DeployResult deploy(TransactionOpts opts, Object... params) throws Exception {
        byte[] bin = bins.toByteArray();
        byte[] paramsBuf = abiExport.pack("Create", params);
        byte[] data = Bytes.concat(bin, paramsBuf);
        Transaction signedTx = transact(opts, null, data);
        if (signedTx == null){
            throw new Exception("sign err");
        }
        DeployResult result = new DeployResult();
        result.setTransactionHash(signedTx.getHash());
        Address contractAddress = AddressUtil.createAddress(opts.getFrom(), signedTx.getNonce());
        result.setContractAddress(contractAddress);
        return result;
    }
    public int getType(){
        return bins.getType();
    }
    public Tuple2<String, ABIExport.EventABIObj> findEventByName(String event){
        return abiExport.findEvent(event);
    }
    public abstract T getCaller(Address address);
    public abstract D getSender(Address address);
    public abstract Event[] events();
    public Event parseEvent(String hash, byte[] data){
        for (Event event : events()){
            if (hash.equals(event.getHash())){
                event.parse(data);
                return event;
            }
        }
        return null;
    }
    public Object call(CallOpts opts, String method, Address contractAddress, Object... params) throws Exception {
        if (opts == null){
            opts = new CallOpts();
        }
        byte[] bin = bins.toByteArray();
        byte[] paramsBuf = abiExport.pack(method, params);
        byte[] data = Bytes.concat(bin, paramsBuf);
        CallRequest request = new CallRequest();
        request.setFrom(opts.from);
        request.setTo(contractAddress);
        request.setData(data);
        byte[] result = service.call(request);
        // vm call
        return abiExport.unpackResult(method, result);
    }
    public Hash send(TransactionOpts opts, String method, Address contractAddress, Object... params) throws Exception {
        byte[] bin = bins.toByteArray();
        byte[] paramsBuf = abiExport.pack(method, params);
        byte[] data = Bytes.concat(bin, paramsBuf);
        Transaction signedTx = transact(opts, contractAddress, data);
        return signedTx.getHash();
    }
}
