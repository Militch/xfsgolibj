package tech.xfs.xfsgolibj.examples;

import tech.xfs.xfsgolibj.common.Hash;

public class TransactionObserver implements MyObserver {
    private final Hash transactionHash;
    private final int confirmNumber;
    TransactionObserver(
            Hash transactionHash,
            int confirmNumber) {
        this.transactionHash = transactionHash;
        this.confirmNumber = confirmNumber;
    }

    public Hash getTransactionHash() {
        return transactionHash;
    }

    public int getConfirmNumber() {
        return confirmNumber;
    }

    @Override
    public void waitForNotify() throws InterruptedException {
        synchronized (this) {
            this.wait();
        }
    }

    @Override
    public void notifySync() {
        synchronized (this) {
            this.notifyAll();
        }
    }
}
