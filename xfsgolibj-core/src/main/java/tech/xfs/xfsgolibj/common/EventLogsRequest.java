package tech.xfs.xfsgolibj.common;

public class EventLogsRequest {
    private Long fromBlock;
    private Long toBlock;
    private String address;
    private String eventHash;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEventHash() {
        return eventHash;
    }

    public void setEventHash(String eventHash) {
        this.eventHash = eventHash;
    }
}
