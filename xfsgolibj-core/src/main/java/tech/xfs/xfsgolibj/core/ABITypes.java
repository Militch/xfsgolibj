package tech.xfs.xfsgolibj.core;

import org.web3j.utils.Numeric;
import tech.xfs.xfsgolibj.common.Address;
import tech.xfs.xfsgolibj.common.Tuple2;
import tech.xfs.xfsgolibj.utils.Binary;
import tech.xfs.xfsgolibj.utils.Bytes;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class ABITypes {
    private static int calcOutLens(int len){
        int blocks = len / 8;
        int mod = len % 8;
        return mod != 0?(blocks+1)*8:(blocks*8);
    }
    private static boolean checkoutNumberOverflow(int n, int b){
        return (n >> b != 0);
    }
    private static boolean checkoutNumberOverflow(long n, int b){
        return (n >> b != 0);
    }
    public static byte[] pack(String type, Object obj) throws Exception {
        if (type == null){
            throw new Exception("type is null");
        }
        if (obj == null){
            return null;
        }
        switch (type){
            case "CTypeString":
                if (!(obj instanceof String)){
                    throw new Exception("type check err");
                }
                int len = ((String) obj).length();
                byte[] stringLenBuf = Binary.LittleEndian.fromInt64(len);
                byte[] stringBuf = ((String) obj).getBytes(Charset.defaultCharset());
                int datalen = stringLenBuf.length + stringBuf.length;
                stringBuf = Bytes.concat(stringLenBuf, stringBuf);
                int outLens = calcOutLens(datalen);
                return Arrays.copyOf(stringBuf, outLens);
            case "CTypeUint8":
                if (!(obj instanceof Integer)){
                    throw new Exception("type check err");
                }
                if (checkoutNumberOverflow((int)obj, 8)){
                    throw new Exception("type number overflow");
                }
                return Arrays.copyOf(new byte[]{(byte) (int) obj}, calcOutLens(1));
            case "CTypeUint16":
                if (!(obj instanceof Integer)){
                    throw new Exception("type check err");
                }
                if (checkoutNumberOverflow((int)obj, 16)){
                    throw new Exception("type number overflow");
                }
                byte[] uint16Buf = Binary.LittleEndian.fromInt16((int) obj);
                return Arrays.copyOf(uint16Buf, calcOutLens(uint16Buf.length));
            case "CTypeUint32":
                if (!(obj instanceof Integer)){
                    throw new Exception("type check err");
                }
                if (checkoutNumberOverflow((int)obj, 32)){
                    throw new Exception("type number overflow");
                }
                byte[] uint32Buf = Binary.LittleEndian.fromInt32((int) obj);
                return Arrays.copyOf(uint32Buf, calcOutLens(uint32Buf.length));
            case "CTypeUint64":
                if (!(obj instanceof Long)){
                    throw new Exception("type check err");
                }
                if (checkoutNumberOverflow((long)obj, 32)){
                    throw new Exception("type number overflow");
                }
                byte[] uint64Buf = Binary.LittleEndian.fromInt64((long) obj);
                return Arrays.copyOf(uint64Buf, calcOutLens(uint64Buf.length));
            case "CTypeUint256":
                if (!(obj instanceof BigInteger)){
                    throw new Exception("type check err");
                }
                byte[] uint256Buf = Numeric.toBytesPadded((BigInteger) obj,32);
                return Arrays.copyOf(uint256Buf, calcOutLens(uint256Buf.length));
            case "CTypeAddress":
                if (!(obj instanceof Address)){
                    throw new Exception("type check err");
                }
                byte[] addressBuf = ((Address) obj).toBytes();
                if (addressBuf == null){
                    return null;
                }
                return Arrays.copyOf(addressBuf, calcOutLens(addressBuf.length));
            case "CTypeBool":
                if (!(obj instanceof Boolean)){
                    throw new Exception("type check err");
                }
                boolean bool = (boolean) obj;
                byte boolByte = bool ? (byte) 1 : (byte) 0;
                return Arrays.copyOf(new byte[]{boolByte}, calcOutLens(1));
        }
        return null;
    }
    public static Object unpack(String type, byte[] data) throws Exception {
        if (type == null || type.isEmpty()){
            throw new IllegalArgumentException("type is nulll");
        }
        if (data == null){
            return null;
        }
        switch (type) {
            case "CTypeString":
                return new String(data, StandardCharsets.UTF_8);
            case "CTypeUint8":
                if (data.length != 1){
                    throw new Exception("result type check err");
                }
                return (int) data[0];
            case "CTypeUint16":
                if (data.length != 2){
                    throw new Exception("result type check err");
                }
                return Binary.LittleEndian.toInt16(data);
            case "CTypeUint32":
                if (data.length != 4){
                    throw new Exception("result type check err");
                }
                return Binary.LittleEndian.toInt32(data);
            case "CTypeUint64":
                if (data.length != 8){
                    throw new Exception("result type check err");
                }
                return Binary.LittleEndian.toInt64(data);
            case "CTypeAddress":
                if (data.length != 25){
                    throw new Exception("result type check err");
                }
                return new Address(data);
            case "CTypeUint256":
                if (data.length != 32){
                    throw new Exception("result type check err");
                }
                return new BigInteger(data);
            case "CTypeBool":
                if (data.length != 1){
                    throw new Exception("result type check err");
                }
                byte boolByte = data[0];
                return boolByte == 1;
        }
        return null;
    }
}
