package tech.xfs.xfsgolibj.core;

import com.google.gson.Gson;
import org.web3j.utils.Numeric;
import tech.xfs.xfsgolibj.common.Address;
import tech.xfs.xfsgolibj.common.Tuple2;
import tech.xfs.xfsgolibj.utils.Binary;
import tech.xfs.xfsgolibj.utils.Bytes;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ABIExport {
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
    public static final class ArgObj {
        private String name;
        private String type;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public byte[] pack(Object obj) throws Exception {
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
    }
    public static final class EventABIObj {
        private String name;
        private int argc;
        private List<ArgObj> args;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getArgc() {
            return argc;
        }

        public void setArgc(int argc) {
            this.argc = argc;
        }

        public List<ArgObj> getArgs() {
            return args;
        }

        public void setArgs(List<ArgObj> args) {
            this.args = args;
        }

    }
    public static final class MethodABIObj {
        private String name;
        private int argc;
        private String returnType;
        private List<ArgObj> args;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getArgc() {
            return argc;
        }

        public void setArgc(int argc) {
            this.argc = argc;
        }

        public List<ArgObj> getArgs() {
            return args;
        }

        public void setArgs(List<ArgObj> args) {
            this.args = args;
        }

        public String getReturnType() {
            return returnType;
        }

        public void setReturnType(String returnType) {
            this.returnType = returnType;
        }
    }
    public static final class ABIOut {
        private Map<String,EventABIObj> events;
        private Map<String,MethodABIObj> methods;

        public Map<String, EventABIObj> getEvents() {
            return events;
        }

        public void setEvents(Map<String, EventABIObj> events) {
            this.events = events;
        }

        public Map<String, MethodABIObj> getMethods() {
            return methods;
        }

        public void setMethods(Map<String, MethodABIObj> methods) {
            this.methods = methods;
        }
    }
    private final ABIOut abiOut;
    private ABIExport(ABIOut abiOut) {
        this.abiOut = abiOut;
    }
    private final static Gson g = new Gson().newBuilder().create();
    public Tuple2<String, EventABIObj> findEvent(String name) {
        if (name == null){
            return null;
        }
        Optional<Map.Entry<String,EventABIObj>> op = abiOut.events.entrySet().stream()
                .filter((item)-> item.getValue().name.equals(name)).findFirst();
        if (!op.isPresent()){
            return null;
        }
        String key = op.get().getKey();
        EventABIObj value = op.get().getValue();
        return new Tuple2<>(key, value);
    }
    public Tuple2<String, MethodABIObj> findMethod(String name) throws Exception {
        if (name == null){
            throw new Exception();
        }
        Optional<Map.Entry<String,MethodABIObj>> op = abiOut.methods.entrySet().stream()
                .filter((item)-> item.getValue().name.equals(name)).findFirst();
        if (!op.isPresent()){
            return null;
        }
        String key = op.get().getKey();
        MethodABIObj value = op.get().getValue();
        return new Tuple2<>(key, value);
    }
    public static ABIExport fromJson(String json) {
        ABIOut abiOut = g.fromJson(json, ABIOut.class);
        if (abiOut == null){
            return null;
        }
        return new ABIExport(abiOut);
    }
    public byte[] pack(String name, Object... params) throws Exception {
        if (name == null || name.length() == 0){
            throw new Exception("method name not null");
        }
        Tuple2<String, MethodABIObj> methodABIObjPair = findMethod(name);
        MethodABIObj methodABIObj = methodABIObjPair.getSecond();
        if (methodABIObj.argc != params.length){
            throw new Exception("method not match");
        }
        String key = methodABIObjPair.getFirst();
        byte[] methodKey = Bytes.hexToBytes(key);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(methodKey);
        for (int i=0;i< methodABIObj.argc; i++) {
            Object param = params[i];
            ArgObj arg = methodABIObj.args.get(i);
            byte[] data = arg.pack(param);
            if (data == null || data.length == 0){
                throw new Exception("require value");
            }
            baos.write(data);
        }
        return baos.toByteArray();
    }
     public Object unpackResult(String method, byte[] data) throws Exception {
        if (data == null){
            return null;
        }
        Tuple2<String,MethodABIObj> methodABIObjPair = findMethod(method);
        MethodABIObj methodABIObj = methodABIObjPair.getSecond();
        switch (methodABIObj.returnType) {
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
