package tech.xfs.xfsgolibj.service;

import org.junit.Test;
import tech.xfs.xfsgolibj.common.*;
import tech.xfs.xfsgolibj.utils.Bytes;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

public class RPCChainServiceTest {
    private final ChainServiceTester tester = new ChainServiceTester();
    @Test
    public void testSendRawTransaction() throws Exception {
        RawTransaction tx = new RawTransaction();
        tx.setVersion(1);
        tx.setTo(Address.fromString("ZP88dodAoiDrnshxhNnMpqM4nwMiXAHak"));
        tx.setGasPrice(BigInteger.ZERO);
        tx.setGasLimit(0L);
        tx.setNonce(0L);
        tx.setValue(BigInteger.ZERO);
        tx.setSignature(new byte[]{0x01});

        Hash hash = tester.sendRawTransaction(
                "{\n" +
                "    \"id\": 0,\n" +
                "    \"jsonrpc\": \"1.0\",\n" +
                "    \"result\": \"0xdb6d2ffb101df242bbda322c855e9b88d6fa9ade7c8d6b733eaad4b9794677e5\"\n" +
                "}", tx);
        String wantHashHex = "0xdb6d2ffb101df242bbda322c855e9b88d6fa9ade7c8d6b733eaad4b9794677e5";
        assert hash.equals(Hash.fromHex(wantHashHex));
    }
    @Test
    public void testVMCall() throws Exception {
        CallRequest callRequest = new CallRequest();
        byte[] data = tester.vmCall("{\n" +
                "    \"id\": 0,\n" +
                "    \"jsonrpc\": \"1.0\",\n" +
                "    \"result\": \"0xff010203\"\n" +
                "}", callRequest);
        byte[] wantData = Bytes.hexToBytes("0xff010203");
        assert Arrays.equals(data, wantData);
    }
    @Test
    public void testGetHead() throws Exception {
        BlockHeader blockHeader = tester.getHead( "{\n" +
                "    \"id\": 1,\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"result\": {\n" +
                "        \"height\": 230,\n" +
                "        \"version\": 0,\n" +
                "        \"hash_prev_block\": \"0x0000009da0373ef1737f52619fa362aa3597d4c66238b65ba74327090816253c\",\n" +
                "        \"timestamp\": 1641185270,\n" +
                "        \"coinbase\": \"YyLpW2o4zTjRDV3rQez6aiLVfgCdbJz4y\",\n" +
                "        \"state_root\": \"0xd911e6ed810598baaf97472792a6a9f9997ea8908ea0253d9abaaa362437f121\",\n" +
                "        \"transactions_root\": \"0x0000000000000000000000000000000000000000000000000000000000000000\",\n" +
                "        \"receipts_root\": \"0x0000000000000000000000000000000000000000000000000000000000000000\",\n" +
                "        \"gas_limit\": 2500000,\n" +
                "        \"gas_used\": 0,\n" +
                "        \"bits\": 4278190109,\n" +
                "        \"nonce\": 74342,\n" +
                "        \"extranonce\": 13886229815286710583,\n" +
                "        \"hash\": \"0x000000f894ee1f675d9ae1918c01aa4d34ea76e64de79929c5b8663e510d9a69\"\n" +
                "    }\n" +
                "}");
        assert blockHeader != null;
        assert blockHeader.getHeight() != null && blockHeader.getHeight().equals(230L);
        assert blockHeader.getVersion() != null && blockHeader.getVersion().equals(0);
        assert blockHeader.getTimestamp() != null && blockHeader.getTimestamp().equals(1641185270L);
        assert blockHeader.getCoinbase() != null && blockHeader.getCoinbase()
                .equals(Address.fromString("YyLpW2o4zTjRDV3rQez6aiLVfgCdbJz4y"));
        assert blockHeader.getStateRoot() != null && blockHeader.getStateRoot()
                .equals(Hash.fromHex("0xd911e6ed810598baaf97472792a6a9f9997ea8908ea0253d9abaaa362437f121"));
        assert blockHeader.getTransactionsRoot() != null && blockHeader.getTransactionsRoot()
                .equals(Hash.fromHex("0x0000000000000000000000000000000000000000000000000000000000000000"));
        assert blockHeader.getReceiptsRoot() != null && blockHeader.getReceiptsRoot()
                .equals(Hash.fromHex("0x0000000000000000000000000000000000000000000000000000000000000000"));
        assert blockHeader.getGasLimit() != null && blockHeader.getGasLimit().equals(2500000L);
        assert blockHeader.getGasUsed() != null && blockHeader.getGasUsed().equals(0L);
        assert blockHeader.getBits() != null && blockHeader.getBits().equals(4278190109L);
        assert blockHeader.getNonce() != null && blockHeader.getNonce().equals(74342L);
        assert blockHeader.getExtraNonce() != null && blockHeader.getExtraNonce()
                .equals(new BigInteger("13886229815286710583"));
        assert blockHeader.getHash() != null && blockHeader.getHash()
                .equals(Hash.fromHex("0x000000f894ee1f675d9ae1918c01aa4d34ea76e64de79929c5b8663e510d9a69"));
    }
    @Test
    public void testGetBlockHeaderByHash() throws Exception {
        BlockHeader blockHeader = tester.getBlockHeaderByHash( "{\n" +
                "    \"id\": 1,\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"result\": {\n" +
                "        \"height\": 230,\n" +
                "        \"version\": 0,\n" +
                "        \"hash_prev_block\": \"0x0000009da0373ef1737f52619fa362aa3597d4c66238b65ba74327090816253c\",\n" +
                "        \"timestamp\": 1641185270,\n" +
                "        \"coinbase\": \"YyLpW2o4zTjRDV3rQez6aiLVfgCdbJz4y\",\n" +
                "        \"state_root\": \"0xd911e6ed810598baaf97472792a6a9f9997ea8908ea0253d9abaaa362437f121\",\n" +
                "        \"transactions_root\": \"0x0000000000000000000000000000000000000000000000000000000000000000\",\n" +
                "        \"receipts_root\": \"0x0000000000000000000000000000000000000000000000000000000000000000\",\n" +
                "        \"gas_limit\": 2500000,\n" +
                "        \"gas_used\": 0,\n" +
                "        \"bits\": 4278190109,\n" +
                "        \"nonce\": 74342,\n" +
                "        \"extranonce\": 13886229815286710583,\n" +
                "        \"hash\": \"0x000000f894ee1f675d9ae1918c01aa4d34ea76e64de79929c5b8663e510d9a69\"\n" +
                "    }\n" +
                "}", Hash.fromHex("0x000000f894ee1f675d9ae1918c01aa4d34ea76e64de79929c5b8663e510d9a69"));
        assert blockHeader != null;
        assert blockHeader.getHeight() != null && blockHeader.getHeight().equals(230L);
        assert blockHeader.getVersion() != null && blockHeader.getVersion().equals(0);
        assert blockHeader.getTimestamp() != null && blockHeader.getTimestamp().equals(1641185270L);
        assert blockHeader.getCoinbase() != null && blockHeader.getCoinbase()
                .equals(Address.fromString("YyLpW2o4zTjRDV3rQez6aiLVfgCdbJz4y"));
        assert blockHeader.getStateRoot() != null && blockHeader.getStateRoot()
                .equals(Hash.fromHex("0xd911e6ed810598baaf97472792a6a9f9997ea8908ea0253d9abaaa362437f121"));
        assert blockHeader.getTransactionsRoot() != null && blockHeader.getTransactionsRoot()
                .equals(Hash.fromHex("0x0000000000000000000000000000000000000000000000000000000000000000"));
        assert blockHeader.getReceiptsRoot() != null && blockHeader.getReceiptsRoot()
                .equals(Hash.fromHex("0x0000000000000000000000000000000000000000000000000000000000000000"));
        assert blockHeader.getGasLimit() != null && blockHeader.getGasLimit().equals(2500000L);
        assert blockHeader.getGasUsed() != null && blockHeader.getGasUsed().equals(0L);
        assert blockHeader.getBits() != null && blockHeader.getBits().equals(4278190109L);
        assert blockHeader.getNonce() != null && blockHeader.getNonce().equals(74342L);
        assert blockHeader.getExtraNonce() != null && blockHeader.getExtraNonce()
                .equals(new BigInteger("13886229815286710583"));
        assert blockHeader.getHash() != null && blockHeader.getHash()
                .equals(Hash.fromHex("0x000000f894ee1f675d9ae1918c01aa4d34ea76e64de79929c5b8663e510d9a69"));
    }
    @Test
    public void testGetBlockHeaderByNumber() throws Exception {
        BlockHeader blockHeader = tester.getBlockHeaderByNumber( "{\n" +
                "    \"id\": 1,\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"result\": {\n" +
                "        \"height\": 230,\n" +
                "        \"version\": 0,\n" +
                "        \"hash_prev_block\": \"0x0000009da0373ef1737f52619fa362aa3597d4c66238b65ba74327090816253c\",\n" +
                "        \"timestamp\": 1641185270,\n" +
                "        \"coinbase\": \"YyLpW2o4zTjRDV3rQez6aiLVfgCdbJz4y\",\n" +
                "        \"state_root\": \"0xd911e6ed810598baaf97472792a6a9f9997ea8908ea0253d9abaaa362437f121\",\n" +
                "        \"transactions_root\": \"0x0000000000000000000000000000000000000000000000000000000000000000\",\n" +
                "        \"receipts_root\": \"0x0000000000000000000000000000000000000000000000000000000000000000\",\n" +
                "        \"gas_limit\": 2500000,\n" +
                "        \"gas_used\": 0,\n" +
                "        \"bits\": 4278190109,\n" +
                "        \"nonce\": 74342,\n" +
                "        \"extranonce\": 13886229815286710583,\n" +
                "        \"hash\": \"0x000000f894ee1f675d9ae1918c01aa4d34ea76e64de79929c5b8663e510d9a69\"\n" +
                "    }\n" +
                "}",1);
        assert blockHeader != null;
        assert blockHeader.getHeight() != null && blockHeader.getHeight().equals(230L);
        assert blockHeader.getVersion() != null && blockHeader.getVersion().equals(0);
        assert blockHeader.getTimestamp() != null && blockHeader.getTimestamp().equals(1641185270L);
        assert blockHeader.getCoinbase() != null && blockHeader.getCoinbase()
                .equals(Address.fromString("YyLpW2o4zTjRDV3rQez6aiLVfgCdbJz4y"));
        assert blockHeader.getStateRoot() != null && blockHeader.getStateRoot()
                .equals(Hash.fromHex("0xd911e6ed810598baaf97472792a6a9f9997ea8908ea0253d9abaaa362437f121"));
        assert blockHeader.getTransactionsRoot() != null && blockHeader.getTransactionsRoot()
                .equals(Hash.fromHex("0x0000000000000000000000000000000000000000000000000000000000000000"));
        assert blockHeader.getReceiptsRoot() != null && blockHeader.getReceiptsRoot()
                .equals(Hash.fromHex("0x0000000000000000000000000000000000000000000000000000000000000000"));
        assert blockHeader.getGasLimit() != null && blockHeader.getGasLimit().equals(2500000L);
        assert blockHeader.getGasUsed() != null && blockHeader.getGasUsed().equals(0L);
        assert blockHeader.getBits() != null && blockHeader.getBits().equals(4278190109L);
        assert blockHeader.getNonce() != null && blockHeader.getNonce().equals(74342L);
        assert blockHeader.getExtraNonce() != null && blockHeader.getExtraNonce()
                .equals(new BigInteger("13886229815286710583"));
        assert blockHeader.getHash() != null && blockHeader.getHash()
                .equals(Hash.fromHex("0x000000f894ee1f675d9ae1918c01aa4d34ea76e64de79929c5b8663e510d9a69"));
    }
    @Test
    public void testGetLogs() throws Exception {
        EventLogsRequest eventLogsRequest = new EventLogsRequest();
        List<EventLog> eventLogList = tester.getLogs("{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"id\": 1,\n" +
                "    \"result\": [\n" +
                "        {\n" +
                "            \"block_number\": 14,\n" +
                "            \"block_hash\": \"0x00000061bc89a7d04edff7487b1b531e8ebf312ea4bbb7b8bc4f7dc345046f53\",\n" +
                "            \"transaction_hash\": \"0x53ca8afba33c501c6a169a80dfd37fa60e1f6aa9abc8d3d26824ec1c8e96fb2b\",\n" +
                "            \"event_hash\": \"0x011f3f6cad22a2efb7ae1c8e484a01b51b384f4dee84a4c4e776d1abbc7ad9e4\",\n" +
                "            \"event_value\": \"0x7b2266726f6d223a223031316332376565633838353239396234376630613465626265363266353435653063666439323830653163336265623139222c22746f223a223031366530646435353834333963663734643939663332643330323061303931343230356531656531366365326435636132222c2276616c7565223a2230303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303034353633393138323434663430303030227d\",\n" +
                "            \"address\": \"hHazw9yQyLqRGWzg4e5fN1ZaVLx3hQ8dQ\"\n" +
                "        }\n" +
                "    ]\n" +
                "}", eventLogsRequest);
        assert eventLogList != null && eventLogList.size() == 1;
        EventLog gotEventLog = eventLogList.get(0);
        assert gotEventLog != null;
        assert gotEventLog.getBlockNumber() != null && gotEventLog.getBlockNumber().equals(14L);
        assert gotEventLog.getBlockHash() != null && gotEventLog.getBlockHash()
                .equals(Hash.fromHex("0x00000061bc89a7d04edff7487b1b531e8ebf312ea4bbb7b8bc4f7dc345046f53"));
        assert gotEventLog.getTransactionHash() != null && gotEventLog.getTransactionHash()
                .equals(Hash.fromHex("0x53ca8afba33c501c6a169a80dfd37fa60e1f6aa9abc8d3d26824ec1c8e96fb2b"));
        assert gotEventLog.getEventHash() != null && gotEventLog.getEventHash()
                .equals(Hash.fromHex("0x011f3f6cad22a2efb7ae1c8e484a01b51b384f4dee84a4c4e776d1abbc7ad9e4"));
        assert gotEventLog.getEventValue() != null && Arrays.equals(gotEventLog.getEventValue(),
                Bytes.hexToBytes("0x7b2266726f6d223a223031316332376565633838353239396234376630613465626265363266353435653063666439323830653163336265623139222c22746f223a223031366530646435353834333963663734643939663332643330323061303931343230356531656531366365326435636132222c2276616c7565223a2230303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303034353633393138323434663430303030227d"));
        assert gotEventLog.getAddress() != null && gotEventLog.getAddress().equals(Address.fromString("hHazw9yQyLqRGWzg4e5fN1ZaVLx3hQ8dQ"));
    }

    @Test
    public void testGetAddressTxNonce() throws Exception {
        Address address = Address.fromString("ZP88dodAoiDrnshxhNnMpqM4nwMiXAHak");
        Long nonce = tester.getAddressTxNonce("{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"id\": 1,\n" +
                "    \"result\": 178\n" +
                "}", address);
        assert nonce != null && nonce.equals(178L);
    }

    @Test
    public void testGetTransactionsByBlockHash() throws Exception {
        Hash blockHash = Hash.fromHex("0x73eb9615a4f5ba80ac18aff2fa1feb30d3b750cd38c4a4cfdb5d482df8e2dd7f");
        List<Transaction> transactions = tester.getTransactionsByBlockHash("{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"id\": 1,\n" +
                "    \"result\": [\n" +
                "        {\n" +
                "            \"version\": 1,\n" +
                "            \"to\": \"bQfi7kVUf2VAUsBk1R9FEzHXdtNtD98bs\",\n" +
                "            \"gas_price\": 10000000000,\n" +
                "            \"gas_limit\": 25000,\n" +
                "            \"nonce\": 0,\n" +
                "            \"value\": 123434545,\n" +
                "            \"from\": \"bQfi7kVUf2VAUsBk1R9FEzHXdtNtD98bs\",\n" +
                "            \"hash\": \"0x73eb9615a4f5ba80ac18aff2fa1feb30d3b750cd38c4a4cfdb5d482df8e2dd7f\",\n" +
                "            \"data\": \"0xd0230100000000000000000000000000000000000000000000000000000000000000000700000000000000555344546573740004000000000000005553445400000000120000000000000000000000000000000000000000000000000000000000003635c9adc5dea00000\",\n" +
                "            \"signature\": \"0xd47196c895989d5d5fb82f3bb178edcb7f34728be9ce5ed4976843b8afd1a1255e0e9f9ded9e8e211278daf23eb418b0f3270136e5946a9ad7b68fdaf2843ea000\"\n" +
                "        }\n" +
                "    ]\n" +
                "}", blockHash);
        assert transactions != null && transactions.size() == 1;
        Transaction gotTransaction = transactions.get(0);
        assert gotTransaction != null;
        assert gotTransaction.getVersion() != null && gotTransaction.getVersion().equals(1);
        assert gotTransaction.getTo() != null && gotTransaction.getTo()
                .equals(Address.fromString("bQfi7kVUf2VAUsBk1R9FEzHXdtNtD98bs"));
        assert gotTransaction.getFrom() != null && gotTransaction.getFrom()
                .equals(Address.fromString("bQfi7kVUf2VAUsBk1R9FEzHXdtNtD98bs"));
        assert gotTransaction.getGasPrice() != null && gotTransaction.getGasPrice()
                .equals(new BigInteger("10000000000"));
        assert gotTransaction.getGasLimit() != null && gotTransaction.getGasLimit()
                .equals(new BigInteger("25000"));
        assert gotTransaction.getNonce() != null && gotTransaction.getNonce().equals(0L);
        assert gotTransaction.getValue() != null && gotTransaction.getValue()
                .equals(new BigInteger("123434545"));
        assert gotTransaction.getHash() != null && gotTransaction.getHash()
                .equals(Hash.fromHex("0x73eb9615a4f5ba80ac18aff2fa1feb30d3b750cd38c4a4cfdb5d482df8e2dd7f"));
        assert gotTransaction.getData() != null && Arrays.equals(gotTransaction.getData(),
                Bytes.hexToBytes("0xd0230100000000000000000000000000000000000000000000000000000000000000000700000000000000555344546573740004000000000000005553445400000000120000000000000000000000000000000000000000000000000000000000003635c9adc5dea00000"));
        assert gotTransaction.getSignature() != null && Arrays.equals(gotTransaction.getSignature(),
                Bytes.hexToBytes("0xd47196c895989d5d5fb82f3bb178edcb7f34728be9ce5ed4976843b8afd1a1255e0e9f9ded9e8e211278daf23eb418b0f3270136e5946a9ad7b68fdaf2843ea000"));
    }

    @Test
    public void testGetTransactionByHash() throws Exception {
        Hash blockHash = Hash.fromHex("0x73eb9615a4f5ba80ac18aff2fa1feb30d3b750cd38c4a4cfdb5d482df8e2dd7f");
        Transaction transaction = tester.getTransactionByHash("{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"id\": 1,\n" +
                "    \"result\": {\n" +
                "        \"version\": 1,\n" +
                "        \"to\": \"bQfi7kVUf2VAUsBk1R9FEzHXdtNtD98bs\",\n" +
                "        \"gas_price\": 10000000000,\n" +
                "        \"gas_limit\": 25000,\n" +
                "        \"nonce\": 0,\n" +
                "        \"value\": 123434545,\n" +
                "        \"from\": \"bQfi7kVUf2VAUsBk1R9FEzHXdtNtD98bs\",\n" +
                "        \"hash\": \"0x73eb9615a4f5ba80ac18aff2fa1feb30d3b750cd38c4a4cfdb5d482df8e2dd7f\",\n" +
                "        \"data\": \"0xd0230100000000000000000000000000000000000000000000000000000000000000000700000000000000555344546573740004000000000000005553445400000000120000000000000000000000000000000000000000000000000000000000003635c9adc5dea00000\",\n" +
                "        \"signature\": \"0xd47196c895989d5d5fb82f3bb178edcb7f34728be9ce5ed4976843b8afd1a1255e0e9f9ded9e8e211278daf23eb418b0f3270136e5946a9ad7b68fdaf2843ea000\"\n" +
                "    }\n" +
                "}", blockHash);
        assert transaction != null;
        assert transaction.getVersion() != null && transaction.getVersion().equals(1);
        assert transaction.getTo() != null && transaction.getTo()
                .equals(Address.fromString("bQfi7kVUf2VAUsBk1R9FEzHXdtNtD98bs"));
        assert transaction.getFrom() != null && transaction.getFrom()
                .equals(Address.fromString("bQfi7kVUf2VAUsBk1R9FEzHXdtNtD98bs"));
        assert transaction.getGasPrice() != null && transaction.getGasPrice()
                .equals(new BigInteger("10000000000"));
        assert transaction.getGasLimit() != null && transaction.getGasLimit()
                .equals(new BigInteger("25000"));
        assert transaction.getNonce() != null && transaction.getNonce().equals(0L);
        assert transaction.getValue() != null && transaction.getValue()
                .equals(new BigInteger("123434545"));
        assert transaction.getHash() != null && transaction.getHash()
                .equals(Hash.fromHex("0x73eb9615a4f5ba80ac18aff2fa1feb30d3b750cd38c4a4cfdb5d482df8e2dd7f"));
        assert transaction.getData() != null && Arrays.equals(transaction.getData(),
                Bytes.hexToBytes("0xd0230100000000000000000000000000000000000000000000000000000000000000000700000000000000555344546573740004000000000000005553445400000000120000000000000000000000000000000000000000000000000000000000003635c9adc5dea00000"));
        assert transaction.getSignature() != null && Arrays.equals(transaction.getSignature(),
                Bytes.hexToBytes("0xd47196c895989d5d5fb82f3bb178edcb7f34728be9ce5ed4976843b8afd1a1255e0e9f9ded9e8e211278daf23eb418b0f3270136e5946a9ad7b68fdaf2843ea000"));
    }

    @Test
    public void testGetReceiptByHash() throws Exception {
        Hash blockHash = Hash.fromHex("0x73eb9615a4f5ba80ac18aff2fa1feb30d3b750cd38c4a4cfdb5d482df8e2dd7f");
        TransactionReceipt receipt = tester.getReceiptByHash("{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"id\": 1,\n" +
                "    \"result\": {\n" +
                "        \"version\": 1,\n" +
                "        \"status\": 1,\n" +
                "        \"tx_hash\": \"0x73eb9615a4f5ba80ac18aff2fa1feb30d3b750cd38c4a4cfdb5d482df8e2dd7f\",\n" +
                "        \"gas_used\": 25000,\n" +
                "        \"block_height\": 1,\n" +
                "        \"block_hash\": \"0x0000008056a2242ae17579c9b98556d28632210ee1096ee03e520f9d1eec403e\",\n" +
                "        \"block_index\": 1,\n" +
                "        \"tx_index\": 0,\n" +
                "        \"logs\": [\n" +
                "            \"0x011f3f6cad22a2efb7ae1c8e484a01b51b384f4dee84a4c4e776d1abbc7ad9e4\"\n" +
                "        ]\n" +
                "    }\n" +
                "}", blockHash);
        assert receipt != null;
        assert receipt.getVersion() != null && receipt.getVersion().equals(1);
        assert receipt.getStatus() != null && receipt.getStatus().equals(1);
        assert receipt.getTxHash() != null && receipt.getTxHash()
                .equals(Hash.fromHex("0x73eb9615a4f5ba80ac18aff2fa1feb30d3b750cd38c4a4cfdb5d482df8e2dd7f"));
        assert receipt.getGasUsed() != null && receipt.getGasUsed()
                .equals(new BigInteger("25000"));
        assert receipt.getBlockHeight() != null && receipt.getBlockHeight().equals(1L);
        assert receipt.getBlockHash() != null && receipt.getBlockHash()
                .equals(Hash.fromHex("0x0000008056a2242ae17579c9b98556d28632210ee1096ee03e520f9d1eec403e"));
        assert receipt.getBlockIndex() != null && receipt.getBlockIndex().equals(1L);
        assert receipt.getTxIndex() != null && receipt.getTxIndex().equals(0L);
        assert receipt.getLogs() != null && receipt.getLogs().size() == 1;
        Hash logHash = receipt.getLogs().get(0);
        assert logHash != null && logHash.equals(
                Hash.fromHex("0x011f3f6cad22a2efb7ae1c8e484a01b51b384f4dee84a4c4e776d1abbc7ad9e4"));
    }
    @Test
    public void testGetAccount() throws Exception {
        Address address = Address.fromString("bQfi7kVUf2VAUsBk1R9FEzHXdtNtD98bs");
        AccountRequest request = new AccountRequest();
        request.setAddress(address);
        Account account = tester.getAccount("{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"id\": 1,\n" +
                "    \"result\": {\n" +
                "        \"balance\": \"20000000000000000000\",\n" +
                "        \"nonce\": 2,\n" +
                "        \"extra\": null,\n" +
                "        \"code\": \"0xaa657890\",\n" +
                "        \"state_root\": \"0x011f3f6cad22a2efb7ae1c8e484a01b51b384f4dee84a4c4e776d1abbc7ad9e4\"\n" +
                "    }\n" +
                "}", request);
        assert account != null;
        assert account.getBalance() != null && account.getBalance()
                .equals(new BigInteger("20000000000000000000"));
        assert account.getNonce() != null && account.getNonce()
                .equals(2L);
        assert account.getCode() != null && Arrays.equals(account.getCode(), Bytes.hexToBytes("0xaa657890"));
        assert account.getStateRoot() != null & account.getStateRoot()
                .equals(Hash.fromHex("0x011f3f6cad22a2efb7ae1c8e484a01b51b384f4dee84a4c4e776d1abbc7ad9e4"));
    }
    @Test
    public void testGetBalance() throws Exception {
        Address address = Address.fromString("bQfi7kVUf2VAUsBk1R9FEzHXdtNtD98bs");
        BigInteger balance = tester.getBalance("{\n" +
                "    \"id\": 1,\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"result\": \"100000000000000000000\"\n" +
                "}", address);
        assert balance.equals(new BigInteger("100000000000000000000"));
    }
    @Test
    public void testGetStorageAt() throws Exception{
        StorageAtRequest request = new StorageAtRequest();
        byte[] data = tester.getStorageAt("{\n" +
                "    \"id\": 1,\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"result\": \"0x0000000000000000000000000000000000000000000000000000000000000005\"\n" +
                "}", request);
        assert data != null && Arrays.equals(data, Bytes.hexToBytes("0x0000000000000000000000000000000000000000000000000000000000000005"));
    }
    @Test
    public void testPendingTransactions() throws Exception {
        List<Transaction> transactions = tester.getPendingTransactions("{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"id\": 1,\n" +
                "    \"result\": [\n" +
                "        {\n" +
                "            \"version\": 1,\n" +
                "            \"to\": \"bQfi7kVUf2VAUsBk1R9FEzHXdtNtD98bs\",\n" +
                "            \"gas_price\": 10000000000,\n" +
                "            \"gas_limit\": 25000,\n" +
                "            \"nonce\": 0,\n" +
                "            \"value\": 123434545,\n" +
                "            \"from\": \"bQfi7kVUf2VAUsBk1R9FEzHXdtNtD98bs\",\n" +
                "            \"hash\": \"0x73eb9615a4f5ba80ac18aff2fa1feb30d3b750cd38c4a4cfdb5d482df8e2dd7f\",\n" +
                "            \"data\": \"0xd0230100000000000000000000000000000000000000000000000000000000000000000700000000000000555344546573740004000000000000005553445400000000120000000000000000000000000000000000000000000000000000000000003635c9adc5dea00000\",\n" +
                "            \"signature\": \"0xd47196c895989d5d5fb82f3bb178edcb7f34728be9ce5ed4976843b8afd1a1255e0e9f9ded9e8e211278daf23eb418b0f3270136e5946a9ad7b68fdaf2843ea000\"\n" +
                "        }\n" +
                "    ]\n" +
                "}");
        assert transactions != null && transactions.size() == 1;
        Transaction gotTransaction = transactions.get(0);
        assert gotTransaction != null;
        assert gotTransaction.getVersion() != null && gotTransaction.getVersion().equals(1);
        assert gotTransaction.getTo() != null && gotTransaction.getTo()
                .equals(Address.fromString("bQfi7kVUf2VAUsBk1R9FEzHXdtNtD98bs"));
        assert gotTransaction.getFrom() != null && gotTransaction.getFrom()
                .equals(Address.fromString("bQfi7kVUf2VAUsBk1R9FEzHXdtNtD98bs"));
        assert gotTransaction.getGasPrice() != null && gotTransaction.getGasPrice()
                .equals(new BigInteger("10000000000"));
        assert gotTransaction.getGasLimit() != null && gotTransaction.getGasLimit()
                .equals(new BigInteger("25000"));
        assert gotTransaction.getNonce() != null && gotTransaction.getNonce().equals(0L);
        assert gotTransaction.getValue() != null && gotTransaction.getValue()
                .equals(new BigInteger("123434545"));
        assert gotTransaction.getHash() != null && gotTransaction.getHash()
                .equals(Hash.fromHex("0x73eb9615a4f5ba80ac18aff2fa1feb30d3b750cd38c4a4cfdb5d482df8e2dd7f"));
        assert gotTransaction.getData() != null && Arrays.equals(gotTransaction.getData(),
                Bytes.hexToBytes("0xd0230100000000000000000000000000000000000000000000000000000000000000000700000000000000555344546573740004000000000000005553445400000000120000000000000000000000000000000000000000000000000000000000003635c9adc5dea00000"));
        assert gotTransaction.getSignature() != null && Arrays.equals(gotTransaction.getSignature(),
                Bytes.hexToBytes("0xd47196c895989d5d5fb82f3bb178edcb7f34728be9ce5ed4976843b8afd1a1255e0e9f9ded9e8e211278daf23eb418b0f3270136e5946a9ad7b68fdaf2843ea000"));
    }
    @Test
    public void testQueueTransactions() throws Exception {
        List<Transaction> transactions = tester.getQueueTransactions("{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"id\": 1,\n" +
                "    \"result\": [\n" +
                "        {\n" +
                "            \"version\": 1,\n" +
                "            \"to\": \"bQfi7kVUf2VAUsBk1R9FEzHXdtNtD98bs\",\n" +
                "            \"gas_price\": 10000000000,\n" +
                "            \"gas_limit\": 25000,\n" +
                "            \"nonce\": 0,\n" +
                "            \"value\": 123434545,\n" +
                "            \"from\": \"bQfi7kVUf2VAUsBk1R9FEzHXdtNtD98bs\",\n" +
                "            \"hash\": \"0x73eb9615a4f5ba80ac18aff2fa1feb30d3b750cd38c4a4cfdb5d482df8e2dd7f\",\n" +
                "            \"data\": \"0xd0230100000000000000000000000000000000000000000000000000000000000000000700000000000000555344546573740004000000000000005553445400000000120000000000000000000000000000000000000000000000000000000000003635c9adc5dea00000\",\n" +
                "            \"signature\": \"0xd47196c895989d5d5fb82f3bb178edcb7f34728be9ce5ed4976843b8afd1a1255e0e9f9ded9e8e211278daf23eb418b0f3270136e5946a9ad7b68fdaf2843ea000\"\n" +
                "        }\n" +
                "    ]\n" +
                "}");
        assert transactions != null && transactions.size() == 1;
        Transaction gotTransaction = transactions.get(0);
        assert gotTransaction != null;
        assert gotTransaction.getVersion() != null && gotTransaction.getVersion().equals(1);
        assert gotTransaction.getTo() != null && gotTransaction.getTo()
                .equals(Address.fromString("bQfi7kVUf2VAUsBk1R9FEzHXdtNtD98bs"));
        assert gotTransaction.getFrom() != null && gotTransaction.getFrom()
                .equals(Address.fromString("bQfi7kVUf2VAUsBk1R9FEzHXdtNtD98bs"));
        assert gotTransaction.getGasPrice() != null && gotTransaction.getGasPrice()
                .equals(new BigInteger("10000000000"));
        assert gotTransaction.getGasLimit() != null && gotTransaction.getGasLimit()
                .equals(new BigInteger("25000"));
        assert gotTransaction.getNonce() != null && gotTransaction.getNonce().equals(0L);
        assert gotTransaction.getValue() != null && gotTransaction.getValue()
                .equals(new BigInteger("123434545"));
        assert gotTransaction.getHash() != null && gotTransaction.getHash()
                .equals(Hash.fromHex("0x73eb9615a4f5ba80ac18aff2fa1feb30d3b750cd38c4a4cfdb5d482df8e2dd7f"));
        assert gotTransaction.getData() != null && Arrays.equals(gotTransaction.getData(),
                Bytes.hexToBytes("0xd0230100000000000000000000000000000000000000000000000000000000000000000700000000000000555344546573740004000000000000005553445400000000120000000000000000000000000000000000000000000000000000000000003635c9adc5dea00000"));
        assert gotTransaction.getSignature() != null && Arrays.equals(gotTransaction.getSignature(),
                Bytes.hexToBytes("0xd47196c895989d5d5fb82f3bb178edcb7f34728be9ce5ed4976843b8afd1a1255e0e9f9ded9e8e211278daf23eb418b0f3270136e5946a9ad7b68fdaf2843ea000"));
    }
    @Test
    public void testGetBlockByHash() throws Exception {
        Hash hash = Hash.fromHex("0x0000039e3b8983b26ae829d4334e60fe050e03b43d153ed928336ab08736519e");
        Block block = tester.getBlockByHash("{\n" +
                "    \"id\": 1,\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"result\": {\n" +
                "        \"height\": 27,\n" +
                "        \"version\": 0,\n" +
                "        \"hash_prev_block\": \"0x00000e05250821998fd37f5d3294a910677e1ed883d6b8f77a23395f1edb6996\",\n" +
                "        \"timestamp\": 1637034387,\n" +
                "        \"coinbase\": \"ctgcna1CHyh7ugd3XKkhAmKoPsvT1yeQR\",\n" +
                "        \"state_root\": \"0x6f20b775dc6c5de59ae68f851718d0a6106ca05987497efc6a3364bdb1ad650d\",\n" +
                "        \"transactions_root\": \"0x65811cbbbe6204b0a5e413a20dd0d4397d8758c7709dfbf2681f1888edb82c64\",\n" +
                "        \"receipts_root\": \"0x11b93943e587b50c4172031d20765b69229578992df7b10394ff5a6749596d83\",\n" +
                "        \"gas_limit\": 2500000,\n" +
                "        \"gas_used\": 25000,\n" +
                "        \"bits\": 1069547550,\n" +
                "        \"nonce\": 162206,\n" +
                "        \"extranonce\": 1129726201923012695,\n" +
                "        \"hash\": \"0x0000039e3b8983b26ae829d4334e60fe050e03b43d153ed928336ab08736519e\",\n" +
                "        \"transactions\": [\n" +
                "            {\n" +
                "                \"version\": 0,\n" +
                "                \"to\": \"ScWia9CEgy3N2zxrhjTG8y3g5YHX57QPa\",\n" +
                "                \"gas_price\": 10000000000,\n" +
                "                \"gas_limit\": 2500000,\n" +
                "                \"nonce\": 0,\n" +
                "                \"value\": 1000000000000000000,\n" +
                "                \"from\": \"ctgcna1CHyh7ugd3XKkhAmKoPsvT1yeQR\",\n" +
                "                \"hash\": \"0x30a81c6bf061c3de41573649d1939c4f90287d560d743eef3af5c52fa8398f3e\",\n" +
                "                \"data\": null,\n" +
                "                \"signature\": \"0xd47196c895989d5d5fb82f3bb178edcb7f34728be9ce5ed4976843b8afd1a1255e0e9f9ded9e8e211278daf23eb418b0f3270136e5946a9ad7b68fdaf2843ea000\"\n" +
                "            }\n" +
                "        ]\n" +
                "    }\n" +
                "}",hash);
        assert block != null;
        assert block.getHeight() != null && block.getHeight().equals(27L);
        assert block.getVersion() != null && block.getVersion().equals(0);
        assert block.getTimestamp() != null && block.getTimestamp().equals(1637034387L);
        assert block.getCoinbase() != null && block.getCoinbase()
                .equals(Address.fromString("ctgcna1CHyh7ugd3XKkhAmKoPsvT1yeQR"));
        assert block.getStateRoot() != null && block.getStateRoot()
                .equals(Hash.fromHex("0x6f20b775dc6c5de59ae68f851718d0a6106ca05987497efc6a3364bdb1ad650d"));
        assert block.getTransactionsRoot() != null && block.getTransactionsRoot()
                .equals(Hash.fromHex("0x65811cbbbe6204b0a5e413a20dd0d4397d8758c7709dfbf2681f1888edb82c64"));
        assert block.getReceiptsRoot() != null && block.getReceiptsRoot()
                .equals(Hash.fromHex("0x11b93943e587b50c4172031d20765b69229578992df7b10394ff5a6749596d83"));
        assert block.getGasLimit() != null && block.getGasLimit().equals(2500000L);
        assert block.getGasUsed() != null && block.getGasUsed().equals(25000L);
        assert block.getBits() != null && block.getBits().equals(1069547550L);
        assert block.getNonce() != null && block.getNonce().equals(162206L);
        assert block.getExtraNonce() != null && block.getExtraNonce()
                .equals(new BigInteger("1129726201923012695"));
        assert block.getHash() != null && block.getHash()
                .equals(Hash.fromHex("0x0000039e3b8983b26ae829d4334e60fe050e03b43d153ed928336ab08736519e"));

        assert block.getTransactions() != null && block.getTransactions().size() == 1;
        Transaction gotTransaction = block.getTransactions().get(0);
        assert gotTransaction != null;
        assert gotTransaction.getVersion() != null && gotTransaction.getVersion().equals(0);
        assert gotTransaction.getTo() != null && gotTransaction.getTo()
                .equals(Address.fromString("ScWia9CEgy3N2zxrhjTG8y3g5YHX57QPa"));
        assert gotTransaction.getFrom() != null && gotTransaction.getFrom()
                .equals(Address.fromString("ctgcna1CHyh7ugd3XKkhAmKoPsvT1yeQR"));
        assert gotTransaction.getGasPrice() != null && gotTransaction.getGasPrice()
                .equals(new BigInteger("10000000000"));
        assert gotTransaction.getGasLimit() != null && gotTransaction.getGasLimit()
                .equals(new BigInteger("2500000"));
        assert gotTransaction.getNonce() != null && gotTransaction.getNonce().equals(0L);
        assert gotTransaction.getValue() != null && gotTransaction.getValue()
                .equals(new BigInteger("1000000000000000000"));
        assert gotTransaction.getHash() != null && gotTransaction.getHash()
                .equals(Hash.fromHex("0x30a81c6bf061c3de41573649d1939c4f90287d560d743eef3af5c52fa8398f3e"));
        assert gotTransaction.getData() == null;
        assert gotTransaction.getSignature() != null && Arrays.equals(gotTransaction.getSignature(),
                Bytes.hexToBytes("0xd47196c895989d5d5fb82f3bb178edcb7f34728be9ce5ed4976843b8afd1a1255e0e9f9ded9e8e211278daf23eb418b0f3270136e5946a9ad7b68fdaf2843ea000"));
    }
    @Test
    public void testGetBlockByNumber() throws Exception {
        Block block = tester.getBlockByNumber("{\n" +
                "    \"id\": 1,\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"result\": {\n" +
                "        \"height\": 27,\n" +
                "        \"version\": 0,\n" +
                "        \"hash_prev_block\": \"0x00000e05250821998fd37f5d3294a910677e1ed883d6b8f77a23395f1edb6996\",\n" +
                "        \"timestamp\": 1637034387,\n" +
                "        \"coinbase\": \"ctgcna1CHyh7ugd3XKkhAmKoPsvT1yeQR\",\n" +
                "        \"state_root\": \"0x6f20b775dc6c5de59ae68f851718d0a6106ca05987497efc6a3364bdb1ad650d\",\n" +
                "        \"transactions_root\": \"0x65811cbbbe6204b0a5e413a20dd0d4397d8758c7709dfbf2681f1888edb82c64\",\n" +
                "        \"receipts_root\": \"0x11b93943e587b50c4172031d20765b69229578992df7b10394ff5a6749596d83\",\n" +
                "        \"gas_limit\": 2500000,\n" +
                "        \"gas_used\": 25000,\n" +
                "        \"bits\": 1069547550,\n" +
                "        \"nonce\": 162206,\n" +
                "        \"extranonce\": 1129726201923012695,\n" +
                "        \"hash\": \"0x0000039e3b8983b26ae829d4334e60fe050e03b43d153ed928336ab08736519e\",\n" +
                "        \"transactions\": [\n" +
                "            {\n" +
                "                \"version\": 0,\n" +
                "                \"to\": \"ScWia9CEgy3N2zxrhjTG8y3g5YHX57QPa\",\n" +
                "                \"gas_price\": 10000000000,\n" +
                "                \"gas_limit\": 2500000,\n" +
                "                \"nonce\": 0,\n" +
                "                \"value\": 1000000000000000000,\n" +
                "                \"from\": \"ctgcna1CHyh7ugd3XKkhAmKoPsvT1yeQR\",\n" +
                "                \"hash\": \"0x30a81c6bf061c3de41573649d1939c4f90287d560d743eef3af5c52fa8398f3e\",\n" +
                "                \"data\": null,\n" +
                "                \"signature\": \"0xd47196c895989d5d5fb82f3bb178edcb7f34728be9ce5ed4976843b8afd1a1255e0e9f9ded9e8e211278daf23eb418b0f3270136e5946a9ad7b68fdaf2843ea000\"\n" +
                "            }\n" +
                "        ]\n" +
                "    }\n" +
                "}", 27L);
        assert block != null;
        assert block.getHeight() != null && block.getHeight().equals(27L);
        assert block.getVersion() != null && block.getVersion().equals(0);
        assert block.getTimestamp() != null && block.getTimestamp().equals(1637034387L);
        assert block.getCoinbase() != null && block.getCoinbase()
                .equals(Address.fromString("ctgcna1CHyh7ugd3XKkhAmKoPsvT1yeQR"));
        assert block.getStateRoot() != null && block.getStateRoot()
                .equals(Hash.fromHex("0x6f20b775dc6c5de59ae68f851718d0a6106ca05987497efc6a3364bdb1ad650d"));
        assert block.getTransactionsRoot() != null && block.getTransactionsRoot()
                .equals(Hash.fromHex("0x65811cbbbe6204b0a5e413a20dd0d4397d8758c7709dfbf2681f1888edb82c64"));
        assert block.getReceiptsRoot() != null && block.getReceiptsRoot()
                .equals(Hash.fromHex("0x11b93943e587b50c4172031d20765b69229578992df7b10394ff5a6749596d83"));
        assert block.getGasLimit() != null && block.getGasLimit().equals(2500000L);
        assert block.getGasUsed() != null && block.getGasUsed().equals(25000L);
        assert block.getBits() != null && block.getBits().equals(1069547550L);
        assert block.getNonce() != null && block.getNonce().equals(162206L);
        assert block.getExtraNonce() != null && block.getExtraNonce()
                .equals(new BigInteger("1129726201923012695"));
        assert block.getHash() != null && block.getHash()
                .equals(Hash.fromHex("0x0000039e3b8983b26ae829d4334e60fe050e03b43d153ed928336ab08736519e"));

        assert block.getTransactions() != null && block.getTransactions().size() == 1;
        Transaction gotTransaction = block.getTransactions().get(0);
        assert gotTransaction != null;
        assert gotTransaction.getVersion() != null && gotTransaction.getVersion().equals(0);
        assert gotTransaction.getTo() != null && gotTransaction.getTo()
                .equals(Address.fromString("ScWia9CEgy3N2zxrhjTG8y3g5YHX57QPa"));
        assert gotTransaction.getFrom() != null && gotTransaction.getFrom()
                .equals(Address.fromString("ctgcna1CHyh7ugd3XKkhAmKoPsvT1yeQR"));
        assert gotTransaction.getGasPrice() != null && gotTransaction.getGasPrice()
                .equals(new BigInteger("10000000000"));
        assert gotTransaction.getGasLimit() != null && gotTransaction.getGasLimit()
                .equals(new BigInteger("2500000"));
        assert gotTransaction.getNonce() != null && gotTransaction.getNonce().equals(0L);
        assert gotTransaction.getValue() != null && gotTransaction.getValue()
                .equals(new BigInteger("1000000000000000000"));
        assert gotTransaction.getHash() != null && gotTransaction.getHash()
                .equals(Hash.fromHex("0x30a81c6bf061c3de41573649d1939c4f90287d560d743eef3af5c52fa8398f3e"));
        assert gotTransaction.getData() == null;
        assert gotTransaction.getSignature() != null && Arrays.equals(gotTransaction.getSignature(),
                Bytes.hexToBytes("0xd47196c895989d5d5fb82f3bb178edcb7f34728be9ce5ed4976843b8afd1a1255e0e9f9ded9e8e211278daf23eb418b0f3270136e5946a9ad7b68fdaf2843ea000"));
    }
    @Test
    public void testGetBlockHashByNumber() throws Exception {
        Hash gotHash = tester.getBlockHashByNumber("{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"id\": 1,\n" +
                "    \"result\": \"0x4af4ce2938a113f8e704151951f14611630718e0871bcc8843b2b40dc1b289db\"\n" +
                "}",0);

        assert gotHash != null;
        assert gotHash.equals(Hash.fromHex("0x4af4ce2938a113f8e704151951f14611630718e0871bcc8843b2b40dc1b289db"));
    }

    @Test
    public void testGetBlockHashesByRange() throws Exception {
        List<Hash> hashes = tester.getBlockHashesByRange("[\n" +
                "    {\n" +
                "        \"jsonrpc\": \"2.0\",\n" +
                "        \"id\": null,\n" +
                "        \"result\": \"0x4af4ce2938a113f8e704151951f14611630718e0871bcc8843b2b40dc1b289db\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"jsonrpc\": \"2.0\",\n" +
                "        \"id\": null,\n" +
                "        \"result\": \"0x0000005dcc1f1df6049ae09f911b3e7abcfa0d6862b1ab611f9392b3aac9244c\"\n" +
                "    }\n" +
                "]", 0,1);
        assert hashes != null && hashes.size() == 2;
        Hash first = hashes.get(0); Hash last = hashes.get(1);
        Hash wantFirst = Hash.fromHex("0x4af4ce2938a113f8e704151951f14611630718e0871bcc8843b2b40dc1b289db");
        Hash wantLast = Hash.fromHex("0x0000005dcc1f1df6049ae09f911b3e7abcfa0d6862b1ab611f9392b3aac9244c");
        assert first.equals(wantFirst);
        assert last.equals(wantLast);
        List<Hash> hashesNeedNull = tester.getBlockHashesByRange("[\n" +
                "    {\n" +
                "        \"jsonrpc\": \"2.0\",\n" +
                "        \"id\": null,\n" +
                "        \"result\": \"0x4af4ce2938a113f8e704151951f14611630718e0871bcc8843b2b40dc1b289db\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"jsonrpc\": \"2.0\",\n" +
                "        \"id\": null,\n" +
                "        \"result\": null\n" +
                "    }\n" +
                "]", 0,1);
        assert hashesNeedNull != null && hashesNeedNull.size() == 1;
    }
}
