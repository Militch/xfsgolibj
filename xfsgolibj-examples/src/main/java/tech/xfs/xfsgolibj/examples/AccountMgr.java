package tech.xfs.xfsgolibj.examples;

import org.checkerframework.checker.units.qual.A;
import org.web3j.crypto.ECKeyPair;
import tech.xfs.xfsgolibj.common.Address;
import tech.xfs.xfsgolibj.common.Hash;
import tech.xfs.xfsgolibj.common.RawTransaction;
import tech.xfs.xfsgolibj.common.Signer;
import tech.xfs.xfsgolibj.core.MyKeys;
import tech.xfs.xfsgolibj.utils.Bytes;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.HashMap;
import java.util.Map;

/**
 * 账户管理器，这里模拟业务数据库
 */
public class AccountMgr implements Signer {
    // 用以记录存储账户密钥
    private final Map<Address, Account> accountMap = new HashMap<>();
    // 用以记录存储账户余额
    private final Map<Address, BigInteger> balanceMap = new HashMap<>();

    /**
     * 随机生成账户
     */
    public Address newAccount() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        ECKeyPair ecKeyPair = MyKeys.createEcKeyPair();
        byte[] addressBytes = MyKeys.getAddress(ecKeyPair.getPublicKey().toByteArray());
        Account account = new Account(new Address(addressBytes), ecKeyPair);
        Address address = new Address(addressBytes);
        accountMap.put(address, account);
        return address;
    }

    public Map<Address, Account> getAccountMap() {
        return accountMap;
    }

    public BigInteger getBalance(Address address){
        return balanceMap.get(address);
    }

    /**
     * 更新账户余额
     * @param address 账户地址
     * @param value 账户余额
     */
    public synchronized void updateAccountBalance(Address address, BigInteger value){
        balanceMap.put(address, value);
    }

    /**
     * 导入账户
     * @param hex 私钥十六进制编码
     */
    public Address importAccount(String hex) throws Exception {
        byte[] keyBytes = Bytes.hexToBytes(hex);
        ECKeyPair ecKeyPair = MyKeys.importPrivateKey(keyBytes);
        byte[] addressBytes = MyKeys.getAddress(ecKeyPair.getPublicKey().toByteArray());
        Account account = new Account(new Address(addressBytes), ecKeyPair);
        Address address = new Address(addressBytes);
        accountMap.put(address, account);
        return address;
    }

    @Override
    public RawTransaction sign(Address from, RawTransaction tx) throws Exception {
        if (!accountMap.containsKey(from)){
            throw new Exception("无法签名，本地找不到该发送地址：" + from.toBase58());
        }
        // 这里使用未签名的hash
        Hash unsignedHash = tx.getUnsignedHash();
        Account account = accountMap.get(from);
        byte[] privateKey = account.getKeyPair().getPrivateKey().toByteArray();
        // 签名交易
        byte[] signature = MyKeys.sign(privateKey, unsignedHash.getData());
        tx.setSignature(signature);
        return tx;
    }
}
