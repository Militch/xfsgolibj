package tech.xfs.xfsgolibj.common;

public class CallRequest {
    private Hash stateRoot;
    private Address from;
    private Address to;
    private byte[] data;

    public Hash getStateRoot() {
        return stateRoot;
    }

    public void setStateRoot(Hash stateRoot) {
        this.stateRoot = stateRoot;
    }

    public Address getFrom() {
        return from;
    }

    public void setFrom(Address from) {
        this.from = from;
    }

    public Address getTo() {
        return to;
    }

    public void setTo(Address to) {
        this.to = to;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

}
