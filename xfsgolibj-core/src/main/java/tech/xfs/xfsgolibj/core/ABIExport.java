package tech.xfs.xfsgolibj.core;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
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
import java.util.stream.Collectors;

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
            return ABITypes.pack(type, obj);
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

        public Object parseEventValue(String argName, byte[] bytes) throws Exception {
            if (argName == null){
                throw new IllegalArgumentException("argName is null");
            }
            ArgObj argObj = args.stream().filter((item)-> item.name.equals(argName)).findFirst().orElse(null);
            if (argObj == null){
                throw new IllegalArgumentException("argObj is null");
            }
            return ABITypes.unpack(argObj.type, bytes);
        }
        public void setArgs(List<ArgObj> args) {
            this.args = args;
        }

    }
    public static final class MethodABIObj {
        private String name;
        private int argc;
        @SerializedName("return_type")
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
        if (methodABIObjPair == null){
            throw new Exception("Not found Method");
        }
        MethodABIObj methodABIObj = methodABIObjPair.getSecond();
        return ABITypes.unpack(methodABIObj.returnType, data);
     }
}
