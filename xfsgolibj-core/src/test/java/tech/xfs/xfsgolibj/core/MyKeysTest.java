package tech.xfs.xfsgolibj.core;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.ECKeyPair;
import tech.xfs.xfsgolibj.utils.Bytes;



public class MyKeysTest {
    private static final Logger logger = LoggerFactory.getLogger(MyKeysTest.class);
    @Test
    public void exportPrivateKey() throws Exception {
        ECKeyPair keyPair = MyKeys.createEcKeyPair();
        String address = MyKeys.getAddressB58String(keyPair.getPublicKey().toByteArray());
        byte[] keyBytes = MyKeys.exportPrivateKey(keyPair);
        String keyString = Bytes.toHexString(keyBytes);
        System.out.printf("keyString: %s%n", keyString);
        System.out.printf("address: %s%n", address);
        byte[] key = Bytes.hexToBytes(keyString);
        ECKeyPair ecKeyPair = MyKeys.importPrivateKey(key);
        String importAddress = MyKeys.getAddressB58String(ecKeyPair.getPublicKey().toByteArray());
        System.out.printf("importAddress: %s%n", importAddress);
        assert importAddress.equals(address);
    }
}
