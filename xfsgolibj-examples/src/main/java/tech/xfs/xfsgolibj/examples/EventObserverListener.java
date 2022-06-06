package tech.xfs.xfsgolibj.examples;

import tech.xfs.xfsgolibj.common.EventLog;
import tech.xfs.xfsgolibj.core.Contract;

public interface EventObserverListener {
    void onCallback(Contract.Event event, byte[] data) throws Exception;
}
