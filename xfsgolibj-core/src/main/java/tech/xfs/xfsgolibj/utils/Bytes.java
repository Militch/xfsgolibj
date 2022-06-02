package tech.xfs.xfsgolibj.utils;

import org.bouncycastle.util.encoders.Hex;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Bytes {
    static Pattern prefixPattern = Pattern.compile("^0[xX]");
    public static byte[] hexToBytes(String hex) {
        if(hex == null || hex.length() == 0){
            return null;
        }
        if (hex.length() % 2 != 0){
            throw new IllegalArgumentException("hex format err");
        }
        String prefix = hex.substring(0, 2);
        Matcher matcher = prefixPattern.matcher(prefix);
        if (matcher.find()){
            hex = hex.substring(2);
        }
        return Hex.decode(hex);
    }
    public static String toHexString(byte[] bs){
        return toHexString(bs, true);
    }
    public static String toHexString(byte[] bs, boolean prefix){
        if (bs == null || bs.length == 0){
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (byte b : bs) {
            int var = b & 0xff;
            String hexvar = Integer.toHexString(var);
            if (hexvar.length() < 2) {
                sb.append(0);
            }
            sb.append(hexvar);
        }
        return (prefix?"0x":"").concat(sb.toString());
    }
    public static byte[] concat(byte[] a, byte[] b){
        byte[] result = Arrays.copyOf(a, a.length + b.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }
}
