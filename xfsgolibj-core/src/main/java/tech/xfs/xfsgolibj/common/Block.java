package tech.xfs.xfsgolibj.common;

import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;
import java.util.List;

public class Block extends BlockHeader {
    @SerializedName("transactions")
    private List<Transaction> transactions;

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}
