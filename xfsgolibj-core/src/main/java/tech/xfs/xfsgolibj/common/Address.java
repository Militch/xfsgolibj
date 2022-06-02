package tech.xfs.xfsgolibj.common;

import java.util.Arrays;

public class Address {
    private static final int ADDRESS_LEN = 25;
    private final byte[] data;
    public Address(byte[] data){
        if (data.length > ADDRESS_LEN){
            data = Arrays.copyOfRange(data, data.length - ADDRESS_LEN,  data.length);
        }
        this.data = Arrays.copyOfRange(data, 0, ADDRESS_LEN);
    }
    public static Address fromString(String text){
        return new Address(Base58.decode(text));
    }
    public byte[] toBytes(){
        return data;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Address)) {
            return false;
        }
        Address target = (Address) obj;
        return Arrays.equals(this.data, target.data);
    }

    public String toBase58(){
        return Base58.encode(data);
    }

    @Override
    public String toString() {
        return toBase58();
    }
}
