package tech.xfs.xfsgolibj.core;


import tech.xfs.xfsgolibj.common.*;

import java.util.List;

public interface ChainService {
    /**
     * 发送原始交易信息
     * @param transaction 标准交易结构
     */
    void sendRawTransaction(RawTransaction transaction) throws Exception;

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
    byte[] call(CallRequest request) throws Exception;

    /**
     * 获取最新区块头
     * @return 区块头结构
     */
    BlockHeader getHead() throws Exception;

    List<Transaction> getTransactionsByBlockHash(Hash hash) throws Exception;

    /**
     * 根据交易 HASH 查询交易信息
     * @param transactionHash 交易 HASH
     * @return 交易信息
     */
    Transaction getTransactionsByHash(Hash transactionHash) throws Exception;

    /**
     * 根据交易 HASH 查询交易回执
     * @param transactionHash 交易HASH
     * @return 回执信息
     */
    TransactionReceipt getReceiptByHash(Hash transactionHash) throws Exception;

    /**
     * 根据条件获取事件日志
     * @param logsRequest 请求条件
     * @return 日志列表
     */
    List<EventLog> getLogs(EventLogsRequest logsRequest) throws Exception;
}
