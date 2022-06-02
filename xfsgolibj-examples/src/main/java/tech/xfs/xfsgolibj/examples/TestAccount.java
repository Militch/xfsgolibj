package tech.xfs.xfsgolibj.examples;

import org.web3j.crypto.ECKeyPair;
import tech.xfs.xfsgolibj.common.Address;
import tech.xfs.xfsgolibj.common.Signer;
import tech.xfs.xfsgolibj.common.Transaction;
import tech.xfs.xfsgolibj.core.MyKeys;
import tech.xfs.xfsgolibj.utils.Bytes;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class TestAccount implements Signer {

    private final Address address;
    private final ECKeyPair keyPair;

    private TestAccount(Address address, ECKeyPair keyPair) {
        this.address = address;
        this.keyPair = keyPair;
    }
    static TestAccount fromHexKey(String hex) throws Exception {
        byte[] keyBytes = Bytes.hexToBytes(hex);
        ECKeyPair ecKeyPair = MyKeys.importPrivateKey(keyBytes);
        byte[] addressBytes = MyKeys.getAddress(ecKeyPair.getPublicKey().toByteArray());
        return new TestAccount(new Address(addressBytes), ecKeyPair);
    }

    static TestAccount createRandom() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        ECKeyPair ecKeyPair = MyKeys.createEcKeyPair();
        byte[] addressBytes = MyKeys.getAddress(ecKeyPair.getPublicKey().toByteArray());
        return new TestAccount(new Address(addressBytes), ecKeyPair);
    }
    public Address getAddress() {
        return address;
    }

    public ECKeyPair getKeyPair() {
        return keyPair;
    }

    @Override
    public Transaction sign(Address from, Transaction tx) throws Exception {
        if (!from.equals(address)){
            throw new Exception("sign address check err");
        }
        byte[] unsignedHash = tx.getUnsignedHash().getData();
        byte[] signature = MyKeys.sign(keyPair.getPrivateKey().toByteArray(), unsignedHash);
        tx.setSignature(signature);
        return tx;
    }

}
