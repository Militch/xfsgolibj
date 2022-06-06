package tech.xfs.xfsgolibj.common;

import com.google.gson.annotations.SerializedName;

public class EventLog {
    @SerializedName("block_number")
    private Long blockNumber;
    @SerializedName("block_Hash")
    private String blockHash;
    @SerializedName("transaction_hash")
    private String transactionHash;
    @SerializedName("event_hash")
    private String eventHash;
    @SerializedName("event_value")
    private String eventValue;
    @SerializedName("address")
    private String address;

    public Long getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(Long blockNumber) {
        this.blockNumber = blockNumber;
    }

    public String getBlockHash() {
        return blockHash;
    }

    public void setBlockHash(String blockHash) {
        this.blockHash = blockHash;
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }

    public String getEventHash() {
        return eventHash;
    }

    public void setEventHash(String eventHash) {
        this.eventHash = eventHash;
    }

    public String getEventValue() {
        return eventValue;
    }

    public void setEventValue(String eventValue) {
        this.eventValue = eventValue;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
