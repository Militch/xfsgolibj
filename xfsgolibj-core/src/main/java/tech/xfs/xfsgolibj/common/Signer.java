package tech.xfs.xfsgolibj.common;

public interface Signer {
    Transaction sign(Address from, Transaction tx) throws Exception;
}
