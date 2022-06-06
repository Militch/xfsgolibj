package tech.xfs.xfsgolibj.common;

import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;

public class BlockHeader {
    @SerializedName("height")
    private Long height;
    @SerializedName("version")
    private Integer version;
    @SerializedName("hash_prev_block")
    private Hash hashPrevBlock;
    @SerializedName("timestamp")
    private Long timestamp;
    @SerializedName("coinbase")
    private Address coinbase;
    @SerializedName("state_root")
    private Hash stateRoot;
    @SerializedName("transactions_root")
    private Hash transactionsRoot;
    @SerializedName("receipts_root")
    private Hash receiptsRoot;
    @SerializedName("gas_limit")
    private Long gasLimit;
    @SerializedName("gas_used")
    private Long gasUsed;
    @SerializedName("bits")
    private Long bits;
    @SerializedName("nonce")
    private Long nonce;
    @SerializedName("extranonce")
    private BigInteger extraNonce;
    @SerializedName("hash")
    private Hash hash;

    public Long getHeight() {
        return height;
    }

    public void setHeight(Long height) {
        this.height = height;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Hash getHashPrevBlock() {
        return hashPrevBlock;
    }

    public void setHashPrevBlock(Hash hashPrevBlock) {
        this.hashPrevBlock = hashPrevBlock;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Address getCoinbase() {
        return coinbase;
    }

    public void setCoinbase(Address coinbase) {
        this.coinbase = coinbase;
    }

    public Hash getStateRoot() {
        return stateRoot;
    }

    public void setStateRoot(Hash stateRoot) {
        this.stateRoot = stateRoot;
    }

    public Hash getTransactionsRoot() {
        return transactionsRoot;
    }

    public void setTransactionsRoot(Hash transactionsRoot) {
        this.transactionsRoot = transactionsRoot;
    }

    public Hash getReceiptsRoot() {
        return receiptsRoot;
    }

    public void setReceiptsRoot(Hash receiptsRoot) {
        this.receiptsRoot = receiptsRoot;
    }

    public Long getGasLimit() {
        return gasLimit;
    }

    public void setGasLimit(Long gasLimit) {
        this.gasLimit = gasLimit;
    }

    public Long getGasUsed() {
        return gasUsed;
    }

    public void setGasUsed(Long gasUsed) {
        this.gasUsed = gasUsed;
    }

    public Long getBits() {
        return bits;
    }

    public void setBits(Long bits) {
        this.bits = bits;
    }

    public Long getNonce() {
        return nonce;
    }

    public void setNonce(Long nonce) {
        this.nonce = nonce;
    }

    public BigInteger getExtraNonce() {
        return extraNonce;
    }

    public void setExtraNonce(BigInteger extraNonce) {
        this.extraNonce = extraNonce;
    }

    public Hash getHash() {
        return hash;
    }

    public void setHash(Hash hash) {
        this.hash = hash;
    }

    @Override
    public String toString() {
        return "BlockHeader{" +
                "height=" + height +
                ", version=" + version +
                ", hashPrevBlock='" + hashPrevBlock + '\'' +
                ", timestamp=" + timestamp +
                ", coinbase='" + coinbase + '\'' +
                ", stateRoot='" + stateRoot + '\'' +
                ", transactionsRoot='" + transactionsRoot + '\'' +
                ", receiptsRoot='" + receiptsRoot + '\'' +
                ", gasLimit=" + gasLimit +
                ", gasUsed=" + gasUsed +
                ", bits=" + bits +
                ", nonce=" + nonce +
                ", extraNonce='" + extraNonce + '\'' +
                ", hash='" + hash + '\'' +
                '}';
    }
}
