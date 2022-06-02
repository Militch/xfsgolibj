package tech.xfs.xfsgolibj.utils;

import org.bouncycastle.crypto.digests.RIPEMD160Digest;
import org.web3j.crypto.Hash;
import tech.xfs.xfsgolibj.common.Address;

import java.util.Arrays;

public class AddressUtil {
    private static final int VERSION = 1;
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

    /**
     * 创建合约地址
     * @param address 账户地址
     * @param nonce 交易号
     * @return 合约地址
     */
    public static Address createAddress(Address address, long nonce){
        if (address == null){
            return null;
        }
        byte[] fromAddress = address.toBytes();
        byte[] addressHash = Hash.sha256(fromAddress);
        byte[] nonceData = Binary.LittleEndian.fromInt64(nonce);
        byte[] mix = Bytes.concat(addressHash, nonceData);
        return getMyAddress(mix);
    }
    private static Address getMyAddress(byte[] pubKey) {
        byte[] pubKeyHash = Hash.sha256(pubKey);
        byte[] pubKeyHashMD = RIPEMD169(pubKeyHash);
        byte[] payload = Bytes.concat(new byte[]{VERSION & 0xff}, pubKeyHashMD);
        byte[] payloadChecksum = calcChecksum(payload);
        byte[] full = Bytes.concat(payload, payloadChecksum);
        return new Address(full);
    }
}
