package tech.xfs.xfsgolibj.common;

import com.google.gson.annotations.SerializedName;

public class BlockHeader {
    @SerializedName("height")
    private Long height;
    @SerializedName("version")
    private Integer version;
    @SerializedName("hash_prev_block")
    private String hashPrevBlock;
    @SerializedName("timestamp")
    private Long timestamp;
    @SerializedName("coinbase")
    private String coinbase;
    @SerializedName("state_root")
    private String stateRoot;
    @SerializedName("transactions_root")
    private String transactionsRoot;
    @SerializedName("receipts_root")
    private String receiptsRoot;
    @SerializedName("gas_limit")
    private Long gasLimit;
    @SerializedName("gas_used")
    private Long gasUsed;
    @SerializedName("bits")
    private Long bits;
    @SerializedName("nonce")
    private Long nonce;
    @SerializedName("extranonce")
    private String extraNonce;
    @SerializedName("hash")
    private String hash;

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

    public String getHashPrevBlock() {
        return hashPrevBlock;
    }

    public void setHashPrevBlock(String hashPrevBlock) {
        this.hashPrevBlock = hashPrevBlock;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getCoinbase() {
        return coinbase;
    }

    public void setCoinbase(String coinbase) {
        this.coinbase = coinbase;
    }

    public String getStateRoot() {
        return stateRoot;
    }

    public void setStateRoot(String stateRoot) {
        this.stateRoot = stateRoot;
    }

    public String getTransactionsRoot() {
        return transactionsRoot;
    }

    public void setTransactionsRoot(String transactionsRoot) {
        this.transactionsRoot = transactionsRoot;
    }

    public String getReceiptsRoot() {
        return receiptsRoot;
    }

    public void setReceiptsRoot(String receiptsRoot) {
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

    public String getExtraNonce() {
        return extraNonce;
    }

    public void setExtraNonce(String extraNonce) {
        this.extraNonce = extraNonce;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
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
