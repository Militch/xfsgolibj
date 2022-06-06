package tech.xfs.xfsgolibj.common;

import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;

public class Account {
    @SerializedName("balance")
    private BigInteger balance;
    @SerializedName("nonce")
    private Long nonce;
    @SerializedName("extra")
    private byte[] extra;
    @SerializedName("code")
    private byte[] code;
    @SerializedName("state_root")
    private Hash stateRoot;

    public BigInteger getBalance() {
        return balance;
    }

    public void setBalance(BigInteger balance) {
        this.balance = balance;
    }

    public Long getNonce() {
        return nonce;
    }

    public void setNonce(Long nonce) {
        this.nonce = nonce;
    }

    public byte[] getExtra() {
        return extra;
    }

    public void setExtra(byte[] extra) {
        this.extra = extra;
    }

    public byte[] getCode() {
        return code;
    }

    public void setCode(byte[] code) {
        this.code = code;
    }

    public Hash getStateRoot() {
        return stateRoot;
    }

    public void setStateRoot(Hash stateRoot) {
        this.stateRoot = stateRoot;
    }
}
