package tech.xfs.xfsgolibj.examples;

import tech.xfs.xfsgolibj.common.Address;
import tech.xfs.xfsgolibj.common.Hash;
import tech.xfs.xfsgolibj.common.Transaction;
import tech.xfs.xfsgolibj.common.TransactionOpts;
import tech.xfs.xfsgolibj.contract.StdTokenContract;
import tech.xfs.xfsgolibj.core.ChainService;
import tech.xfs.xfsgolibj.core.Contract;
import tech.xfs.xfsgolibj.core.RPCClient;
import tech.xfs.xfsgolibj.jsonrpc.HTTPRPCClient;
import tech.xfs.xfsgolibj.service.RPCChainService;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class StdTokenExample {
    private static final RPCClient rpcClient = new HTTPRPCClient(Common.API_URL);
    private static final ChainService chainService = new RPCChainService(rpcClient);

    static final class USDTCoin extends StdTokenContract {
        // 代币名称
        private static final String NAME = "USD Test";
        // 代币符号
        private static final String SYMBOL = "USDT";
        // 代币小数精度
        private static final int DECIMALS = 18;
        // 初始发行量
        private final BigInteger initSupply = BigInteger.valueOf(1000000L);
        public USDTCoin(RPCClient client) {
            super(client);
        }
        public DeployResult deploy(TransactionOpts opts) throws Exception {
            return super.deploy(opts, NAME, SYMBOL, DECIMALS, initSupply);
        }
    }


    /**
     * 等待交易确认
     * @param txHash 交易Hash
     * @param num 确认高度次数
     */
    private static void waitTransactionConfirm(Hash txHash, int num){


    }

    static final class TokenInternalTransaction {
        /**
         * 交易发送地址
         */
        final Address from;
        /**
         * 交易目标地址
         */
        final Address to;
        /**
         * 交易额度
         */
        final BigInteger value;

        public TokenInternalTransaction(Address from, Address to, BigInteger value) {
            this.from = from;
            this.to = to;
            this.value = value;
        }
    }
    /**
     * 测试模拟同步器
     */
    static final class TestSynchronizer {
        // 模拟数据库，记录通证余额。相当于业务的账户表
        final Map<Address, BigInteger> balanceMap = new HashMap<>();
        // 模拟数据库，记录通证内部交易。相当于业务交易记录表
        private final Map<Hash, TokenInternalTransaction> internal = new HashMap<>();
        public void listenLoopAsync() {
            // chainService.getHead();

        }
    }
    public static void main(String[] args) throws Exception {

        // 构造测试同步器
        TestSynchronizer synchronizer = new TestSynchronizer();
        // 启动异步轮询同步
        synchronizer.listenLoopAsync();
        // 初始化合约创建者私钥
        String creatorKey = "";
        TestAccount creator = TestAccount.fromHexKey(creatorKey);
        // 使用创建者账户部署合约
        TransactionOpts deployOpts = new TransactionOpts();
        deployOpts.setFrom(creator.getAddress());
        deployOpts.setSigner(creator);
        USDTCoin usdtCoin = new USDTCoin(rpcClient);
        // 部署 USDT 代币合约
        Contract.DeployResult result = usdtCoin.deploy(deployOpts);
        // 获取合约地址
        Address contractAddress = result.getContractAddress();
        // 等待交易执行成功并且经过 2 次高度确认。这里示例，具体高度确认根据网络情况判断调整
        waitTransactionConfirm(result.getTransactionHash(), 2);
        // 获取合约调用器, 注：Caller 对象不需要交易确认
        StdTokenContract.Caller caller = usdtCoin.getCaller(contractAddress);
        // 查询账户的 USDT 通证余额
        BigInteger creatorBalance = caller.BalanceOf(null, creator.getAddress());

        // 构建账户1
        String account1Key = "";
        TestAccount account1 = TestAccount.fromHexKey(account1Key);
        // 查询账户1余额
        BigInteger account1Balance = caller.BalanceOf(null, account1.getAddress());

        // 获取合约发送器，注：Sender 对象需要发送交易，故需要账户私钥签名
        StdTokenContract.Sender sender = usdtCoin.getSender(contractAddress);

        // 使用合约创建者向 账户1 转移代币
        Hash hash = sender.Transfer(deployOpts, account1.getAddress(), BigInteger.valueOf(1000));
        // 等待交易确认
        waitTransactionConfirm(hash, 2);
        // 查询 账户1 余额
        account1Balance = caller.BalanceOf(null, account1.getAddress());


    }
}
