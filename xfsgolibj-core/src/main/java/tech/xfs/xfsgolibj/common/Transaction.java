package tech.xfs.xfsgolibj.common;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class Transaction {
    @SerializedName("hash")
    private String hash;
    @SerializedName("version")
    private Integer version;
    @SerializedName("to")
    private String to;
    @SerializedName("from")
    private String from;
    @SerializedName("gas_price")
    private BigDecimal gasPrice;
    @SerializedName("gas_limit")
    private BigDecimal gasLimit;
    @SerializedName("nonce")
    private Long nonce;
    @SerializedName("value")
    private BigDecimal value;
    @SerializedName("data")
    private String data;
    @SerializedName("signature")
    private String signature;

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public BigDecimal getGasPrice() {
        return gasPrice;
    }

    public void setGasPrice(BigDecimal gasPrice) {
        this.gasPrice = gasPrice;
    }

    public BigDecimal getGasLimit() {
        return gasLimit;
    }

    public void setGasLimit(BigDecimal gasLimit) {
        this.gasLimit = gasLimit;
    }

    public Long getNonce() {
        return nonce;
    }

    public void setNonce(Long nonce) {
        this.nonce = nonce;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
