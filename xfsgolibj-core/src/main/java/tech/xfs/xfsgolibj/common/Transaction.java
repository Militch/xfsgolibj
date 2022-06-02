package tech.xfs.xfsgolibj.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.xfs.xfsgolibj.utils.Bytes;
import tech.xfs.xfsgolibj.utils.MapUtil;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Transaction {
    private static final Logger logger = LoggerFactory.getLogger(Transaction.class);
    private Integer version;
    private Address to;
    private BigInteger value;
    private BigInteger gasPrice;
    private Long gasLimit;
    private byte[] data;
    private Long nonce;
    private byte[] signature;

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Address getTo() {
        return to;
    }

    public void setTo(Address to) {
        this.to = to;
    }

    public BigInteger getValue() {
        return value;
    }

    public void setValue(BigInteger value) {
        this.value = value;
    }

    public BigInteger getGasPrice() {
        return gasPrice;
    }

    public void setGasPrice(BigInteger gasPrice) {
        this.gasPrice = gasPrice;
    }

    public Long getGasLimit() {
        return gasLimit;
    }

    public void setGasLimit(Long gasLimit) {
        this.gasLimit = gasLimit;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Long getNonce() {
        return nonce;
    }

    public void setNonce(Long nonce) {
        this.nonce = nonce;
    }

    public byte[] getSignature() {
        return signature;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    public Map<String,String> getSerializeMap(boolean isUnsigned){
        if (data == null && to == null){
            throw new IllegalArgumentException("data or to is null");
        }
        Map<String, String> map = new HashMap<>();
        if (version == null){
            throw new IllegalArgumentException("version is null");
        }
        map.put("version", Integer.toString(version));
        map.put("to", to == null?null:to.toBase58());
        if (gasPrice == null){
            throw new IllegalArgumentException("gasPrice is null");
        }
        map.put("gas_price", gasPrice.toString(10));
        if (gasLimit == null){
            throw new IllegalArgumentException("gasLimit is null");
        }
        map.put("gas_limit", Long.toString(gasLimit));
        map.put("data", Bytes.toHexString(data));
        if (nonce == null){
            throw new IllegalArgumentException("nonce is null");
        }
        map.put("nonce", Long.toString(nonce));
        if (value == null){
            throw new IllegalArgumentException("value is null");
        }
        map.put("value", value.toString(10));
        if (isUnsigned){
            return map;
        }
        if (signature == null){
            throw new IllegalArgumentException("signature is null");
        }
        map.put("signature", Bytes.toHexString(signature, false));
        return map;
    }
    public String getSerializeString(boolean isUnsigned){
        Map<String,String> map = getSerializeMap(isUnsigned);
        return MapUtil.sortAndSerialize(map, null);
    }
    public Hash getUnsignedHash(){
        String serialized = getSerializeString(true);
        byte[] bytes = org.web3j.crypto.Hash.sha256(serialized.getBytes(StandardCharsets.UTF_8));
        return new Hash(bytes);
    }
    public Hash getHash(){
        String serialized = getSerializeString(false);
        byte[] bytes = org.web3j.crypto.Hash.sha256(serialized.getBytes(StandardCharsets.UTF_8));
        return new Hash(bytes);
    }

}
