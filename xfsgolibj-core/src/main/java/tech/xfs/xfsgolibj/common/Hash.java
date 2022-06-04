package tech.xfs.xfsgolibj.common;


import tech.xfs.xfsgolibj.utils.Bytes;

import java.util.Arrays;

public final class Hash {
    private static final int HASH_LEN = 32;
    private final byte[] data;
    public Hash(byte[] data){
        if (data.length > HASH_LEN){
            data = Arrays.copyOfRange(data, data.length - HASH_LEN,  data.length);
        }
        this.data = Arrays.copyOfRange(data, 0, HASH_LEN);
    }
    public static Hash fromHex(String text){
        byte[] data = Bytes.hexToBytes(text);
        return new Hash(data);
    }

    public String toHexString(){
        return Bytes.toHexString(data);
    }

    public byte[] getData() {
        return data;
    }

    @Override
    public String toString() {
        return toHexString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Hash)) {
           return false;
        }
        Hash target = (Hash) obj;
        return Arrays.equals(this.data, target.data);
    }
    @Override
    public int hashCode() {
        return data==null? 0: Arrays.hashCode(data);
    }
}
