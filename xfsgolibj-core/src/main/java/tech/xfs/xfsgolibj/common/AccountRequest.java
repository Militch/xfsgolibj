package tech.xfs.xfsgolibj.common;

public class AccountRequest {
    private Hash stateRoot;
    private Address address;

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
}
