package tech.xfs.xfsgolibj.core;

import org.bouncycastle.crypto.digests.RIPEMD160Digest;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;
import tech.xfs.xfsgolibj.common.Base58;
import tech.xfs.xfsgolibj.common.SecureRandomUtils;
import tech.xfs.xfsgolibj.utils.Bytes;

import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.Arrays;

public class MyKeys {
    private static final Logger logger = LoggerFactory.getLogger(MyKeys.class);
    private static final int VERSION = 1;
    private static final int EXPORT_TYPE = 1;
    static {
        if (Security.getProvider("BC") == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }
    static KeyPair createSecp256k1KeyPair() throws NoSuchProviderException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        return createSecp256k1KeyPair(SecureRandomUtils.secureRandom());
    }
    private static byte[] RIPEMD169(byte[] sha256){
        RIPEMD160Digest digest = new RIPEMD160Digest();
        digest.update(sha256, 0, sha256.length);
        byte[] out = new byte[20];
        digest.doFinal(out, 0);
        return out;
    }
    private static byte[] calcChecksum(byte[] payload) {
        byte[] first = Hash.sha256(payload);
        byte[] second = Hash.sha256(first);
        return Arrays.copyOf(second, 4);
    }
    static KeyPair createSecp256k1KeyPair(SecureRandom random) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("ECDSA", "BC");
        ECGenParameterSpec ecGenParameterSpec = new ECGenParameterSpec("secp256k1");
        if (random != null) {
            keyPairGenerator.initialize(ecGenParameterSpec, random);
        } else {
            keyPairGenerator.initialize(ecGenParameterSpec);
        }

        return keyPairGenerator.generateKeyPair();
    }

    /**
     * 导出私钥
     * @param keyPair 密钥对
     * @return 私钥编码
     */
    public static byte[] exportPrivateKey(ECKeyPair keyPair){
        byte[] privateKey = Numeric.toBytesPadded(keyPair.getPrivateKey(), 32);
        return Bytes.concat(new byte[]{VERSION & 0xFF, EXPORT_TYPE & 0xFF}, privateKey);
    }

    /**
     * 导入私钥
     * @param data 私钥编码
     * @return 密钥对
     */
    public static ECKeyPair importPrivateKey(byte[] data) throws Exception {
        if(data == null || data.length < 34){
            throw new Exception("data err");
        }
        byte version = data[0]; byte type = data[1];
        if (version != (VERSION & 0xFF)){
            throw new Exception("version not match");
        }
        if (type != (EXPORT_TYPE & 0xFF)){
            throw new Exception("type not match");
        }
        byte[] privateKey = Arrays.copyOfRange(data, 2, data.length);
        return ECKeyPair.create(privateKey);
    }

    /**
     * 使用默认的随机生成器创建密钥对
     * @return 密钥对
     */
    public static ECKeyPair createEcKeyPair() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        return createEcKeyPair(SecureRandomUtils.secureRandom());
    }

    /**
     * 根据随机生成器创建密钥对
     * @param random 随机生成器
     * @return 密钥对
     */
    public static ECKeyPair createEcKeyPair(SecureRandom random) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        KeyPair keyPair = createSecp256k1KeyPair(random);
        return ECKeyPair.create(keyPair);
    }

    /**
     * 根据公钥导出地址
     * @param publicKey 公钥
     * @return 地址源数据
     */
    public static byte[] getAddress(byte[] publicKey){
        byte[] pubKeyHash = Hash.sha256(publicKey);
        byte[] pubKeyHashMD = RIPEMD169(pubKeyHash);
        byte[] payload = Bytes.concat(new byte[]{VERSION & 0xFF}, pubKeyHashMD);
        byte[] payloadChecksum = calcChecksum(payload);
        return Bytes.concat(payload, payloadChecksum);
    }

    /**
     * 根据公钥导出字符串地址（BASE58 编码）
     * @param publicKey 公钥
     * @return 编码字符串
     */
    public static String getAddressB58String(byte[] publicKey){
        byte[] bytes = getAddress(publicKey);
        return Base58.encode(bytes);
    }
    public static byte[] sign(byte[] privateKey, byte[] dataHash){
        if (privateKey == null){
            throw new IllegalArgumentException("private key is null");
        }
        if (dataHash == null){
            throw new IllegalArgumentException("dataHash is null");
        }
        String address = getAddressB58String(privateKey);
        logger.debug("Sign transaction from: {}", address);
        ECKeyPair ecKeyPair = ECKeyPair.create(privateKey);
        Sign.SignatureData signatureData = Sign.signMessage(dataHash, ecKeyPair, false);
        byte[] r = signatureData.getR();
        byte[] s = signatureData.getS();
        byte[] v = signatureData.getV();
        v[0] = (byte) (v[0] - 27);
        byte[] retval = new byte[65];
        System.arraycopy(r, 0, retval, 0, 32);
        System.arraycopy(s, 0,retval,32, 32);
        System.arraycopy(v, 0,retval,64, 1);
        String signatureHex = Bytes.toHexString(retval);
        logger.debug("signature hex: {}", signatureHex);
        return retval;
    }
}
