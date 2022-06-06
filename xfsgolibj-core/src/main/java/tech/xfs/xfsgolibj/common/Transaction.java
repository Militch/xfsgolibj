package tech.xfs.xfsgolibj.common;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Transaction {
    @SerializedName("hash")
    private Hash hash;
    @SerializedName("version")
    private Integer version;
    @SerializedName("to")
    private Address to;
    @SerializedName("from")
    private Address from;
    @SerializedName("gas_price")
    private BigInteger gasPrice;
    @SerializedName("gas_limit")
    private BigInteger gasLimit;
    @SerializedName("nonce")
    private Long nonce;
    @SerializedName("value")
    private BigInteger value;
    @SerializedName("data")
    private byte[] data;
    @SerializedName("signature")
    private byte[] signature;

    public Hash getHash() {
        return hash;
    }

    public void setHash(Hash hash) {
        this.hash = hash;
    }

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

    public Address getFrom() {
        return from;
    }

    public void setFrom(Address from) {
        this.from = from;
    }

    public BigInteger getGasPrice() {
        return gasPrice;
    }

    public void setGasPrice(BigInteger gasPrice) {
        this.gasPrice = gasPrice;
    }

    public BigInteger getGasLimit() {
        return gasLimit;
    }

    public void setGasLimit(BigInteger gasLimit) {
        this.gasLimit = gasLimit;
    }

    public Long getNonce() {
        return nonce;
    }

    public void setNonce(Long nonce) {
        this.nonce = nonce;
    }

    public BigInteger getValue() {
        return value;
    }

    public void setValue(BigInteger value) {
        this.value = value;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public byte[] getSignature() {
        return signature;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }
}
