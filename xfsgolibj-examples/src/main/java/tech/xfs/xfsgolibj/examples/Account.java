package tech.xfs.xfsgolibj.examples;

import org.web3j.crypto.ECKeyPair;
import tech.xfs.xfsgolibj.common.Address;
import tech.xfs.xfsgolibj.common.Signer;
import tech.xfs.xfsgolibj.common.RawTransaction;
import tech.xfs.xfsgolibj.core.MyKeys;
import tech.xfs.xfsgolibj.utils.Bytes;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class Account implements Signer {

    private final Address address;
    private final ECKeyPair keyPair;

    Account(Address address, ECKeyPair keyPair) {
        this.address = address;
        this.keyPair = keyPair;
    }
    static Account fromHexKey(String hex) throws Exception {
        byte[] keyBytes = Bytes.hexToBytes(hex);
        ECKeyPair ecKeyPair = MyKeys.importPrivateKey(keyBytes);
        byte[] addressBytes = MyKeys.getAddress(ecKeyPair.getPublicKey().toByteArray());
        return new Account(new Address(addressBytes), ecKeyPair);
    }

    static Account createRandom() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        ECKeyPair ecKeyPair = MyKeys.createEcKeyPair();
        byte[] addressBytes = MyKeys.getAddress(ecKeyPair.getPublicKey().toByteArray());
        return new Account(new Address(addressBytes), ecKeyPair);
    }
    public Address getAddress() {
        return address;
    }

    public ECKeyPair getKeyPair() {
        return keyPair;
    }

    @Override
    public RawTransaction sign(Address from, RawTransaction tx) throws Exception {
        if (!from.equals(address)){
            throw new Exception("sign address check err");
        }
        byte[] unsignedHash = tx.getUnsignedHash().getData();
        byte[] signature = MyKeys.sign(keyPair.getPrivateKey().toByteArray(), unsignedHash);
        tx.setSignature(signature);
        return tx;
    }

}
