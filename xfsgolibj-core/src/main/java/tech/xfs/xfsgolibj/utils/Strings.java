package tech.xfs.xfsgolibj.utils;

import tech.xfs.xfsgolibj.common.Address;
import tech.xfs.xfsgolibj.common.Hash;

public class Strings {
    public static String valueOf(Long n){
        return n == null?null:String.valueOf(n);
    }
    public static String valueOf(Address n){
        return n == null?null:n.toBase58();
    }
    public static String valueOf(Hash n){
        return n == null?null:n.toHexString();
    }
}
