package tech.xfs.xfsgolibj.common;

public class StorageAtRequest {
    private Hash stateRoot;
    private Address address;
    private Hash key;

    public Hash getStateRoot() {
        return stateRoot;
    }

    public void setStateRoot(Hash stateRoot) {
        this.stateRoot = stateRoot;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Hash getKey() {
        return key;
    }

    public void setKey(Hash key) {
        this.key = key;
    }
}
