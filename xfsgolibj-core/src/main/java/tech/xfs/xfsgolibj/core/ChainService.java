package tech.xfs.xfsgolibj.core;


import tech.xfs.xfsgolibj.common.*;

import java.math.BigInteger;
import java.util.List;

public interface ChainService {
    /**
     * 发送原始交易信息
     * @param transaction 标准交易结构
     */
    Hash sendRawTransaction(RawTransaction transaction) throws Exception;

    /**
     * 获取账户交易数量
     * @param address 账户地址
     * @return 交易数量
     */
    Long getAddressTxNonce(Address address) throws Exception;

    /**
     * 执行合约方法
     * @param request 请求参数
     * @return 执行结果
     */
    byte[] vmCall(CallRequest request) throws Exception;

    /**
     * 获取最新区块头
     * @return 区块头结构
     */
    BlockHeader getHead() throws Exception;

    /**
     * 根据区块高度获取区块头信息
     * @param number 区块高度
     * @return 区块头
     */
    BlockHeader getBlockHeaderByNumber(long number) throws Exception;

    /**
     * 根据区块Hash查询区块头信息
     * @param hash 区块hash
     * @return 区块头
     */
    BlockHeader getBlockHeaderByHash(Hash hash) throws Exception;

    List<Transaction> getTransactionsByBlockHash(Hash hash) throws Exception;

    /**
     * 根据交易 HASH 查询交易信息
     * @param transactionHash 交易 HASH
     * @return 交易信息
     */
    Transaction getTransactionByHash(Hash transactionHash) throws Exception;

    /**
     * 根据交易 HASH 查询交易回执
     * @param transactionHash 交易HASH
     * @return 回执信息
     */
    TransactionReceipt getReceiptByHash(Hash transactionHash) throws Exception;

    /**
     * 根据条件获取事件日志
     * @param logsRequest 请求参数
     * @return 日志列表
     */
    List<EventLog> getLogs(EventLogsRequest logsRequest) throws Exception;

    /**
     * 获取指定地址的账户信息
     * @param request 请求参数
     * @return 账户信息
     */
    Account getAccount(AccountRequest request) throws Exception;

    /**
     * 获取指定地址的账户余额
     * @param address 地址
     * @return 账户余额
     */
    BigInteger getBalance(Address address) throws Exception;

    /**
     * 获取存储空间的值
     * @param request 请求参数
     * @return 原始值
     */
    byte[] getStorageAt(StorageAtRequest request) throws Exception;

    /**
     * 获取交易池等待中的交易列表
     * @return 交易列表
     */
    List<Transaction> getPendingTransactions() throws Exception;

    /**
     * 获取交易池队列中的交易列表
     * @return 交易列表
     */
    List<Transaction> getQueueTransactions() throws Exception;

    /**
     * 获取指定区块哈希的区块信息
     * @param hash 区块哈希
     * @return 区块信息
     */
    Block getBlockByHash(Hash hash) throws Exception;

    /**
     *  获取指定区块高度的区块信息
     * @param number 区块高度
     * @return 区块信息
     */
    Block getBlockByNumber(Long number) throws Exception;

    /**
     * 获取指定高度的区块哈希
     * @param number 高度
     * @return 区块hash
     */
    Hash getBlockHashByNumber(long number) throws Exception;

    /**
     * 获取指定范围内的所有区块哈希列表
     * @param start 开始高度
     * @param end 结束高度
     * @return 区块哈希列表
     */
    List<Hash> getBlockHashesByRange(long start, long end) throws Exception;
}
