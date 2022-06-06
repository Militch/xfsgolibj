package tech.xfs.xfsgolibj.common;

public class EventLogsRequest {
    private Long fromBlock;
    private Long toBlock;
    private Address address;
    private Hash eventHash;

    public Long getFromBlock() {
        return fromBlock;
    }

    public void setFromBlock(Long fromBlock) {
        this.fromBlock = fromBlock;
    }

    public Long getToBlock() {
        return toBlock;
    }

    public void setToBlock(Long toBlock) {
        this.toBlock = toBlock;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Hash getEventHash() {
        return eventHash;
    }

    public void setEventHash(Hash eventHash) {
        this.eventHash = eventHash;
    }
}
