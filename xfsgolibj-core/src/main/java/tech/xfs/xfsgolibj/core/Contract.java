package tech.xfs.xfsgolibj.core;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.xfs.xfsgolibj.common.*;
import tech.xfs.xfsgolibj.service.RPCChainService;
import tech.xfs.xfsgolibj.utils.AddressUtil;
import tech.xfs.xfsgolibj.utils.Bytes;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public abstract class Contract<T extends Contract.Caller, D extends Contract.Sender> {
    private static final Gson g = new Gson().newBuilder().create();
    private static final Logger logger = LoggerFactory.getLogger(Contract.class);
    private static final BigDecimal DEFAULT_GAS_PRICE = BigDecimal.valueOf(10);
    private static final Long DEFAULT_GAS_LIMIT = 25000L;
    private final BINs bins;
    private final ABIExport abiExport;
    private final ChainService service;
    private final List<Event> events = new ArrayList<>();
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
        final String name;
        final Contract<?,?> contract;
        final Tuple2<String, ABIExport.EventABIObj> eventABIObjPair;
        public Event(Contract<?,?> contract, String eventName) {
            this.contract = contract;
            this.name = eventName;
            eventABIObjPair = this.contract.findEventByName(eventName);
            if (eventABIObjPair == null){
                throw new IllegalArgumentException("not found event");
            }
        }
        public String getName(){
            return name;
        }
        public Hash getHash(){
            return Hash.fromHex(eventABIObjPair.getFirst());
        }
        protected Object parseEventValue(String valueName, byte[] value) throws Exception {
            if(valueName == null || valueName.isEmpty()){
                throw new IllegalArgumentException("valueName is null");
            }
            ABIExport.EventABIObj eventABIObj = eventABIObjPair.getSecond();
            if (eventABIObj == null){
                throw new IllegalArgumentException("eventABIObj == null");
            }
            if(value == null || value.length == 0){
                return null;
            }
            String jsonString = new String(value, StandardCharsets.UTF_8);
            Map<String, String> jsonObj = g.fromJson(jsonString,new TypeToken<Map<String, String>>(){}.getType());
            if (!jsonObj.containsKey(valueName)){
                throw new IllegalArgumentException("not found valueName");
            }
            String valueData = jsonObj.get(valueName);
            if (valueData == null||valueData.isEmpty()){
                return null;
            }
            byte[] fieldValue = Bytes.hexToBytes(valueData);
            if (fieldValue == null || fieldValue.length == 0){
                return null;
            }
            valueName = valueName.substring(0,1).toUpperCase().concat(valueName.substring(1));
            return eventABIObj.parseEventValue(valueName, fieldValue);
        }
    }
    public Contract(RPCClient client, BINs bins, ABIExport abiExport, Class<?>[] events){
        this.bins = bins;
        this.abiExport = abiExport;
        this.service = new RPCChainService(client);
        if (events == null){
            return;
        }
        for (Class<?> eventClass : events){
            if (Event.class.isAssignableFrom(eventClass)){
                try {
                    Constructor<?> declaredConstructors = eventClass.getDeclaredConstructor(Contract.class);
                    Event event = (Event) declaredConstructors.newInstance(this);
                    this.events.add(event);

                } catch (Exception e){
                    // no catch
                }
            }
        }
        // this.events = events;
    }
    private Long getNonce(TransactionOpts opts) throws Exception {
        return service.getAddressTxNonce(opts.getFrom());
    }
    private RawTransaction createStdTransaction(TransactionOpts opts, Address contractAddress, byte[] data) throws Exception {
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
        RawTransaction stdtx = new RawTransaction();
        stdtx.setVersion(1);
        stdtx.setValue(value);
        stdtx.setGasPrice(gasPriceAtto);
        stdtx.setGasLimit(gasLimit);
        stdtx.setNonce(nonce);
        stdtx.setTo(contractAddress);
        stdtx.setData(data);
        return stdtx;
    }
    private RawTransaction transact(TransactionOpts opts, Address contractAddress, byte[] data) throws Exception {
        RawTransaction tx = createStdTransaction(opts, contractAddress, data);
        Signer signer = opts.getSigner();
        if (signer == null){
            throw new Exception("Signer is null");
        }
        logger.debug("Contract transact request sign: {}", g.toJson(tx.getSerializeMap(true)));
        RawTransaction signedTx = signer.sign(opts.getFrom(), tx);
        logger.debug("Contract transact signed transaction: {}", g.toJson(tx.getSerializeMap(false)));
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
        RawTransaction signedTx = transact(opts, null, data);
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
    public Event getEvent(Hash hash){
        if (hash == null){
            return null;
        }
        for (Event event : events){
            if (hash.equals(event.getHash())){
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
        byte[] result = service.vmCall(request);
        // vm call
        return abiExport.unpackResult(method, result);
    }
    public Hash send(TransactionOpts opts, String method, Address contractAddress, Object... params) throws Exception {
        byte[] bin = bins.toByteArray();
        byte[] paramsBuf = abiExport.pack(method, params);
        byte[] data = Bytes.concat(bin, paramsBuf);
        RawTransaction signedTx = transact(opts, contractAddress, data);
        return signedTx.getHash();
    }

    public Hash getEventHash(String eventName){
        if(eventName == null){
            return null;
        }
        for (Event event : events){
            if (eventName.equals(event.name)){
                return event.getHash();
            }
        }
        return null;
    }
}
