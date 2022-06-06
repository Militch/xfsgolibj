package tech.xfs.xfsgolibj.examples;

import tech.xfs.xfsgolibj.common.Address;
import tech.xfs.xfsgolibj.common.EventLog;
import tech.xfs.xfsgolibj.common.Hash;
import tech.xfs.xfsgolibj.core.Contract;

public class EventLogObserver implements MyObserver {
    private EventObserverListener eventObserverListener;
    // 合约地址
    private final Address contractAddress;
    // 事件HASH
    private final Hash eventHash;

    public EventLogObserver(Address contractAddress, Hash eventHash) {
        this.contractAddress = contractAddress;
        this.eventHash = eventHash;
    }

    public EventObserverListener getEventObserverListener() {
        return eventObserverListener;
    }

    public void setEventObserverListener(EventObserverListener eventObserverListener) {
        this.eventObserverListener = eventObserverListener;
    }

    public void notifyASync(Contract.Event event, byte[] data) throws Exception {
        if(eventObserverListener != null){
            eventObserverListener.onCallback(event, data);
        }
    }

    public Address getContractAddress() {
        return contractAddress;
    }

    public Hash getEventHash() {
        return eventHash;
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
