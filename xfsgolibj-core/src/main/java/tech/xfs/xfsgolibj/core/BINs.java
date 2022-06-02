package tech.xfs.xfsgolibj.core;


import tech.xfs.xfsgolibj.utils.Binary;
import tech.xfs.xfsgolibj.utils.Bytes;

import java.util.Arrays;

public class BINs {
    private static final int MAGIC_NUMBER_XVM = 9618;
    private final byte[] data;

    private BINs(byte[] data) {
        this.data = data;
    }
    public boolean isXVM(){
        byte[] part = Arrays.copyOfRange(data, 0, 2);
        int number = Binary.LittleEndian.toInt16(part);
        return number == MAGIC_NUMBER_XVM;
    }
    public int getType(){
        return (int) data[2] & 0xff;
    }
    public byte[] toByteArray(){
        return data;
    }
    public static BINs fromHex(String hex) {
        byte[] data = Bytes.hexToBytes(hex);
        return new BINs(data);
    }
}
