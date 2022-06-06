package tech.xfs.xfsgolibj.examples;

public interface MyObserver {

    void waitForNotify() throws InterruptedException;
    void notifySync();
}
