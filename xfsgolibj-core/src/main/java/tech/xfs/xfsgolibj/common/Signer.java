package tech.xfs.xfsgolibj.common;

public interface Signer {
    RawTransaction sign(Address from, RawTransaction tx) throws Exception;
}
