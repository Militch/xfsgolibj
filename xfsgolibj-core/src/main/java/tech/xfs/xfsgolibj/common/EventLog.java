package tech.xfs.xfsgolibj.common;

import com.google.gson.annotations.SerializedName;

public class EventLog {
    @SerializedName("block_number")
    private Long blockNumber;
    @SerializedName("block_hash")
    private Hash blockHash;
    @SerializedName("transaction_hash")
    private Hash transactionHash;
    @SerializedName("event_hash")
    private Hash eventHash;
    @SerializedName("event_value")
    private byte[] eventValue;
    @SerializedName("address")
    private Address address;

    public Long getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(Long blockNumber) {
        this.blockNumber = blockNumber;
    }

    public Hash getBlockHash() {
        return blockHash;
    }

    public void setBlockHash(Hash blockHash) {
        this.blockHash = blockHash;
    }

    public Hash getTransactionHash() {
        return transactionHash;
    }

    public void setTransactionHash(Hash transactionHash) {
        this.transactionHash = transactionHash;
    }

    public Hash getEventHash() {
        return eventHash;
    }

    public void setEventHash(Hash eventHash) {
        this.eventHash = eventHash;
    }

    public byte[] getEventValue() {
        return eventValue;
    }

    public void setEventValue(byte[] eventValue) {
        this.eventValue = eventValue;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
