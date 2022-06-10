package tech.xfs.xfsgolibj.examples;

import org.checkerframework.checker.units.qual.C;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.xfs.xfsgolibj.common.*;
import tech.xfs.xfsgolibj.contract.StdTokenContract;
import tech.xfs.xfsgolibj.core.ChainService;
import tech.xfs.xfsgolibj.core.Contract;
import tech.xfs.xfsgolibj.core.RPCClient;
import tech.xfs.xfsgolibj.jsonrpc.HTTPRPCClient;
import tech.xfs.xfsgolibj.service.RPCChainService;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class StdTokenExample {
    private static final RPCClient rpcClient = new HTTPRPCClient(Common.API_URL);
    private static final ChainService chainService = new RPCChainService(rpcClient);

    static final class USDTCoin extends StdTokenContract {
        // 代币名称
        private static final String NAME = "USD_XFS";
        // 代币符号
        private static final String SYMBOL = "USDX";
        // 代币小数精度
        public static final int DECIMALS = 18;
        // 初始发行量
        private final BigInteger initSupply = BigInteger.valueOf(1000L)
                .multiply(BigInteger.TEN.pow(DECIMALS));

        public USDTCoin(RPCClient client) {
            super(client);
        }

        public DeployResult deploy(TransactionOpts opts) throws Exception {
            return deploy(opts, NAME, SYMBOL, DECIMALS, initSupply);
        }
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
    static final class TestSynchronizer extends TimerTask {
        // 最后一次同步的区块高度
        private Long lastSyncBlockHeight;
        // 最后一次同步的区块hash
        private Hash lastSyncBlockHash;
        private static final Logger logger = LoggerFactory.getLogger(TestSynchronizer.class);
        // 两个线程池用于处理不同的业务
        // 这个线程池用于处理交易
        private static final ExecutorService processPoll1 = Executors.newCachedThreadPool();
        // 这个线程池用于处理日志
        private static final ExecutorService processPoll2 = Executors.newCachedThreadPool();
        private final Map<Hash, TokenInternalTransaction> internal = new HashMap<>();
        // 交易订阅器，交易hash-订阅对象
        private final Map<Hash, List<TransactionObserver>> txObservers = new HashMap<>();
        // 合约事件订阅器, 合约地址-订阅列表
        private final Map<Address, List<EventLogObserver>> eventObservers = new HashMap<>();
        private final Lock lock = new ReentrantLock();
        private final StdTokenContract contract;

        TestSynchronizer(USDTCoin contract) {
            this.contract = contract;
        }


        public Timer listenLoopAsync() {
            Timer timer = new Timer();
            // 指定一个定时器，每隔1秒执行任务
            timer.schedule(this, 0, 10000);
            return timer;
        }


        private Runnable processTransactionObserves(BlockHeader blockHeader) {
            return ()->{
                if (blockHeader == null){
                    return;
                }
                Long currentBlockHeight = blockHeader.getHeight();
                lock.lock();
                for (Hash transactionHash : txObservers.keySet()) {
                    // 查询交易回执
                    try {
                        TransactionReceipt receipt = chainService.getReceiptByHash(transactionHash);
                        // 回执所在的区块高度
                        Long receiptBlockIndex = receipt.getBlockIndex();
                        List<TransactionObserver> observers = txObservers.get(transactionHash);
                        if (observers == null) {
                            continue;
                        }
                        for (TransactionObserver observer : observers) {
                            // 获取确认次数
                            int confirmNumber = observer.getConfirmNumber();
                            // 期望确认的高度 = 回执高度 + 确认次数
                            long wantConfirmHeight = receiptBlockIndex + confirmNumber;
                            // 对比当前区块高度是否大于或等于期望确认的高度
                            if (currentBlockHeight >= wantConfirmHeight) {
                                // 通知
                                observer.notifySync();
                            }
                        }
                    } catch (Exception e) {
                        // no catch
                    }
                }
                lock.unlock();
            };
        }

        private Runnable processBlockLogsRunnable(BlockHeader blockHeader, List<EventLog> logs) {
            return () -> {
                if (logs == null) {
                    return;
                }
                // 这里因为是异步的原因，习惯性加锁，难免 Observers 在执行过程中发生变化
                lock.lock();
                // 遍历事件日志
                for (EventLog log : logs) {
                    try {
                        // 事件HASH
                        Hash eventHash = log.getEventHash();
                        // 这是发生事件的合约地址
                        // 解析事件
                        Contract.Event event = contract.getEvent(eventHash);
                        if (event == null) {
                            continue;
                        }

                        Address contractAddress = log.getAddress();
                        List<EventLogObserver> observers = eventObservers.get(contractAddress);
                        if (observers == null) {
                            continue;
                        }
                        for (EventLogObserver observer : observers) {
                            if (observer == null) {
                                continue;
                            }
                            if (observer.getEventHash().equals(eventHash)) {
                                observer.notifyASync(event, log.getEventValue());
                            }
                        }
                    } catch (Exception e) {
                        // no catch
                        e.printStackTrace();
                    }
                }
                lock.unlock();
            };
        }

        private void onBlockChange(BlockHeader blockHeader) throws Exception {
            // 到这里，就意味着区块链状态改变了
            // logger.info("区块链发生了改变: {}", blockHeader);
            // 查询当前区块日志
            EventLogsRequest eventLogsRequest = new EventLogsRequest();
            Long height = blockHeader.getHeight();
            // 这里减去2个高度，是因为同步有时间差
            // 所以往前推2个高度获取日志，这个值根据同步速度来调整
            long fromHeight = height - 2;
            eventLogsRequest.setFromBlock(fromHeight < 0 ? 0 : fromHeight);
            eventLogsRequest.setToBlock(blockHeader.getHeight());
            List<EventLog> logs = chainService.getLogs(eventLogsRequest);
            // 这里是去处理交易订阅器
            processPoll1.execute(processTransactionObserves(blockHeader));
            // 解析日志
            processPoll2.execute(processBlockLogsRunnable(blockHeader, logs));
        }

        /**
         * 订阅交易确认消息
         *
         * @param hash  交易 HASH
         * @param count 确认次数
         * @return 观察者对象
         */
        public TransactionObserver subscribeTransactionConfirm(Hash hash, int count) {
            // 这里因为是异步会去读 Observers map（线程不安全） 对象，所以习惯性加锁
            lock.lock();
            // 实例化订阅者
            TransactionObserver observer = new TransactionObserver(hash, count);
            List<TransactionObserver> observers;
            if (!txObservers.containsKey(hash)) {
                observers = new ArrayList<>();
            } else {
                observers = txObservers.get(hash);
            }
            // 将订阅者加入订阅列表
            observers.add(observer);
            // 加入交易
            txObservers.put(hash, observers);
            lock.unlock();
            return observer;
        }

        /**
         * 订阅合约事件
         *
         * @param contractAddress 合约地址
         * @param eventHash       事件哈希
         * @return 观察者对象
         */
        public EventLogObserver subscribeContractEvent(Address contractAddress, Hash eventHash) {
            // 这里因为是异步会去读 Observers map（线程不安全） 对象，所以习惯性加锁
            lock.lock();
            // 实例化订阅者
            EventLogObserver eventLogObserver = new EventLogObserver(contractAddress, eventHash);
            List<EventLogObserver> observers;
            if (!eventObservers.containsKey(contractAddress)) {
                observers = new ArrayList<>();
            } else {
                observers = eventObservers.get(contractAddress);
            }
            // 将订阅者加入订阅列表
            observers.add(eventLogObserver);
            // 加入交易
            eventObservers.put(contractAddress, observers);
            lock.unlock();
            return eventLogObserver;
        }

        @Override
        public void run() {
            try {
                // 这里会不断的去轮询消息检查区块是否改变
                BlockHeader blockHeader = chainService.getHead();
                Long height = blockHeader.getHeight();
                Hash hash = blockHeader.getHash();
                // 这里会比较上一次同步的区块高度和区块hash，如果任意值有改变, 则触发执行 onBlockChange
                if (!height.equals(lastSyncBlockHeight) || !hash.equals(lastSyncBlockHash)) {
                    onBlockChange(blockHeader);
                    // 并且改变最后同步的状态。
                    // 因为是定时器的原因，没有异步执行
                    // （下一个事件（10秒）会等待上一个事件执行成功）
                    // 所以这里的示例不需要加锁！
                    lastSyncBlockHeight = height;
                    lastSyncBlockHash = hash;
                }
            } catch (Exception e) {
                logger.error("Runner error", e);
                // 这里发生异常了要取消所有同步阻塞
                for (Hash key : txObservers.keySet()) {
                    List<TransactionObserver> observers = txObservers.get(key);
                    if (observers == null) {
                        continue;
                    }
                    for (TransactionObserver observer : observers) {
                        observer.notifySync();
                    }
                }

            }
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(StdTokenExample.class);

    private static void usage() {
        System.out.println("Usage: [command] [options] \n" +
                "newAccount                                    创建账户\n" +
                "importAccount <key>                           导入账户私钥\n" +
                "accounts                                      本地账户列表\n" +
                "transfer <from> <to> <n>                      使用 from 地址向 to 地址转移 n 个代币\n" +
                "transferFrom <spender> <from> <to> <n>        使用 spender 地址 从 from 地址向 to 地址转移 n 个代币（需要授额）\n" +
                "approve <from> <spender> <n>                  使用 from 地址授予消费者（spender）可转移额度\n" +
                "burn <address> <n>                            销毁指定地址(address) 拥有的 n 个代币\n" +
                "allowance <owner> <spender>                   查询代币持有者(owner)授予消费者(spender)可转移额度\n" +
                "balanceOf <address>                           查询指定地址的余额\n" +
                "mint <address> <n>                            给指定的地址增发 n 个代币\n" +
                "totalSupply                                   查询当前合约总供给量\n" +
                "name                                          查询当前合约名称\n" +
                "decimals                                      查询当前合约小数精度\n" +
                "symbol                                        查询当前合约符号\n" +
                "contract                                      查询当前合约地址\n" +
                "q|quit                                        退出\n" +
                "help|h|?                                      帮助"
        );
    }

    public static void main(String[] args) throws Exception {
        // 初始化账户管理器-模拟后台
        StdTokenAccountMgr accountMgr = new StdTokenAccountMgr();
        // 实例化 USDTCoin 合约对象
        USDTCoin usdtCoin = new USDTCoin(rpcClient);
        // 构造测试同步器
        TestSynchronizer synchronizer = new TestSynchronizer(usdtCoin);
        // 启动异步轮询同步
        Timer timer = synchronizer.listenLoopAsync();
        // 初始化合约创建者私钥
        String creatorKey = "0x010164ed4696c0681b9c6978e3b87176ebaccc2c24af46b262db50fcb8dbb351ede1";
        Account creator = Account.fromHexKey(creatorKey);
        // 导入默认账户
        accountMgr.importAccount(creatorKey);
        // 以下为构建部署参数
        TransactionOpts deployOpts = new TransactionOpts();
        // 这里要传入部署人的地址
        deployOpts.setFrom(creator.getAddress());
        // 这里要传签名器，在示例代码已经封装了简易的账户签名器仅限参考!!!
        deployOpts.setSigner(creator);
        logger.debug("正在部署合约，创建者地址: {}", creator.getAddress());
        // 部署代币合约，要带入部署参数
        Contract.DeployResult result = usdtCoin.deploy(deployOpts);
        // 获取合约地址
        Address contractAddress = result.getContractAddress();
        // 获取交易HASH
        Hash transactionHash = result.getTransactionHash();
        logger.debug("合约 {} 部署消息已发送，等待上链打包以及交易 {} 确认, 当前确认次数: {}",
                contractAddress.toBase58(), result.getTransactionHash(), Common.DEFAULT_CONFIRM_COUNT);

        StdTokenContract.Sender sender = usdtCoin.getSender(contractAddress);
        StdTokenContract.Caller caller = usdtCoin.getCaller(contractAddress);
        // 创建一个合约事件订阅器-订阅当前合约的转移事件
        Hash transferEventHash = usdtCoin.getEventHash(StdTokenContract.StdTokenTransferEvent.EVENT_NAME);
        EventLogObserver eventLogObserver = synchronizer.subscribeContractEvent(contractAddress, transferEventHash);
        // 设置事件回调
        eventLogObserver.setEventObserverListener((event, data) -> {
            // 如果合约发生了转移事件，那么就应该去更新余额
            StdTokenContract.StdTokenTransferEvent stdTokenTransferEvent =
                    (StdTokenContract.StdTokenTransferEvent) event;
            // 这是转移事件的发送地址
            Address fromAddress = stdTokenTransferEvent.getFrom(data);
            if (fromAddress != null){
                // 更新发送地址的余额
                BigInteger fromAddressBalance = caller.BalanceOf(null, fromAddress);
                accountMgr.updateAccountBalance(fromAddress, fromAddressBalance);
            }
            // 这是转移事件的目标地址
            Address toAddress = stdTokenTransferEvent.getTo(data);
            if (toAddress != null){
                BigInteger toAddressBalance = caller.BalanceOf(null, toAddress);
                // 更新目标地址的余额
                accountMgr.updateAccountBalance(toAddress, toAddressBalance);
            }
            // 获取转移事件的数量
            BigInteger value = stdTokenTransferEvent.getValue(data);
            logger.debug("接受转移事件，发送地址：{}，目标地址：{}, 转移数量：{}", fromAddress, toAddress, value);
        });
        System.out.printf("正在部署合约：%s, 合约创建者：%s%n", contractAddress, creator.getAddress());
        System.out.printf("交易哈希：%s, 当前交易确认次数: %d%n", transactionHash, Common.DEFAULT_CONFIRM_COUNT);
        // 等待交易执行成功并且经过高度确认。这里示例（默认为 2 次），具体高度确认根据网络情况判断调整
        TransactionObserver observer = synchronizer.subscribeTransactionConfirm(
                transactionHash, Common.DEFAULT_CONFIRM_COUNT);
        // 同步等待交易确认成功
        observer.waitForNotify();
        out:
        while (true) {
            try {
                System.out.print("(请输入命令，或 ? 查看帮助)> ");
                Scanner scanner = new Scanner(System.in);
                if (!scanner.hasNext()) {
                    continue;
                }
                scanner.useDelimiter("\n");
                String input = scanner.next();
                if (input == null || input.isEmpty()) {
                    usage();
                    continue;
                }
                String[] inputs = input.split(" ");
                String command = inputs[0];
                String[] options = Arrays.copyOfRange(inputs, 1, inputs.length);
                switch (command) {
                    case "q":
                    case "quit":
                        break out;
                    default:
                    case "?":
                    case "h":
                    case "help":
                        usage();
                        break;
                    case "callMint":
                        if(options.length < 3){
                            usage();
                            break;
                        }
                        Address callMintFromAddress = Address.fromString(options[0]);
                        Address callMintToAddress = Address.fromString(options[1]);
                        BigDecimal callMintValue = new BigDecimal(options[2]);
                        BigInteger realCallMintValue = callMintValue.multiply(
                                        BigDecimal.TEN.pow(USDTCoin.DECIMALS))
                                .toBigInteger();
                        Contract.CallOpts callOpts = new Contract.CallOpts();
                        callOpts.setFrom(callMintFromAddress);
                        Boolean callMintResult = caller.Mint(callOpts, callMintToAddress, realCallMintValue);
                        System.out.printf("%s%n", callMintResult);
                        break;
                    case"allowance":
                        // 查询授权额度
                        if(options.length < 2){
                            usage();
                            break;
                        }
                        Address allowanceOwnerAddress = Address.fromString(options[0]);
                        Address allowanceSpenderAddress = Address.fromString(options[1]);
                        BigInteger allowanceValue = caller.Allowance(null,
                                allowanceOwnerAddress, allowanceSpenderAddress);
                        // 同样展示的时候要除以精度
                        BigDecimal allowanceValueN = allowanceValue==null?BigDecimal.ZERO:
                                new BigDecimal(allowanceValue).divide(BigDecimal.TEN.pow(USDTCoin.DECIMALS),
                                        6, RoundingMode.DOWN);
                        System.out.printf("%s%n", allowanceValueN);
                        break;
                    case "burn":
                        // 销毁代币
                        if(options.length < 2){
                            usage();
                            break;
                        }
                        Address burnAddress = Address.fromString(options[0]);
                        BigDecimal burnValue = new BigDecimal(options[1]);
                        BigInteger realBurnValue = burnValue.multiply(
                                        BigDecimal.TEN.pow(USDTCoin.DECIMALS))
                                .toBigInteger();
                        TransactionOpts burnOpts = new TransactionOpts();
                        // 这里同样需要创建者权限，否则不成功
                        burnOpts.setFrom(creator.getAddress());
                        burnOpts.setSigner(accountMgr);
                        System.out.printf("正在销毁地址 (%s) 代币数量: %s%n", burnAddress, realBurnValue);
                        Hash burnTransactionHash = sender.Burn(burnOpts, burnAddress, realBurnValue);
                        // 订阅交易确认消息
                        TransactionObserver burnTransferObserver = synchronizer.subscribeTransactionConfirm(
                                burnTransactionHash, Common.DEFAULT_CONFIRM_COUNT);
                        // 等待交易确认
                        burnTransferObserver.waitForNotify();
                        // 获取回执结果，判定是否执行成功
                        TransactionReceipt burnReceipt = chainService.getReceiptByHash(burnTransactionHash);
                        Integer burnReceiptStatus = burnReceipt.getStatus();
                        if (burnReceiptStatus.equals(1)){
                            System.out.println("销毁成功！！！");
                        }else{
                            System.out.println("销毁失败！！！");
                        }
                        break;
                    case "approve":
                        // 授额
                        if(options.length < 3){
                            usage();
                            break;
                        }
                        Address approveFromAddress = Address.fromString(options[0]);
                        Address approveSpenderAddress = Address.fromString(options[1]);
                        BigDecimal approveValue = new BigDecimal(options[2]);
                        BigInteger realApproveValue = approveValue.multiply(
                                        BigDecimal.TEN.pow(USDTCoin.DECIMALS))
                                .toBigInteger();
                        TransactionOpts approveOpts = new TransactionOpts();
                        approveOpts.setFrom(approveFromAddress);
                        approveOpts.setSigner(accountMgr);
                        System.out.printf("正在使用地址 (%s) 授予地址 (%s) 可转移代币数量: %s%n",
                                approveFromAddress, approveSpenderAddress, realApproveValue);
                        Hash approveTransactionHash = sender.Approve(approveOpts,
                                approveSpenderAddress, realApproveValue);
                        // 订阅交易确认消息
                        TransactionObserver approveTransferObserver = synchronizer.subscribeTransactionConfirm(
                                approveTransactionHash, Common.DEFAULT_CONFIRM_COUNT);
                        // 等待交易确认
                        approveTransferObserver.waitForNotify();

                        // 获取回执结果，判定是否执行成功
                        TransactionReceipt approveReceipt = chainService.getReceiptByHash(approveTransactionHash);
                        Integer approveReceiptStatus = approveReceipt.getStatus();
                        if (approveReceiptStatus.equals(1)){
                            System.out.println("授权成功！！！");
                        }else{
                            System.out.println("授权失败！！！");
                        }
                        break;
                    case "decimals":
                        // 查询小数进度
                        Integer decimals = caller.GetDecimals(null);
                        System.out.printf("%d%n", decimals);
                        break;
                    case "name":
                        // 查询合约名称
                        String name = caller.GetName(null);
                        System.out.printf("%s%n", name);
                        break;
                    case "symbol":
                        // 查询合约符号
                        String symbol = caller.GetSymbol(null);
                        System.out.printf("%s%n", symbol);
                        break;
                    case "contract":
                        // 查询合约地址
                        System.out.printf("%s%n", contractAddress);
                        break;
                    case "totalSupply":
                        // 查询总供给量
                        BigInteger totalSupply = caller.GetTotalSupply(null);
                        // 同样展示的时候要除以精度
                        BigDecimal totalSupplyN = totalSupply==null?BigDecimal.ZERO:
                                new BigDecimal(totalSupply).divide(BigDecimal.TEN.pow(USDTCoin.DECIMALS),
                                        6, RoundingMode.DOWN);
                        System.out.printf("%s%n", totalSupplyN);
                        break;
                    case "mint":
                        // 给某个地址增发
                        if(options.length < 2){
                            usage();
                            break;
                        }
                        Address mintAddress = Address.fromString(options[0]);
                        BigDecimal mintValue = new BigDecimal(options[1]);
                        BigInteger realMintValue = mintValue.multiply(
                                        BigDecimal.TEN.pow(USDTCoin.DECIMALS))
                                .toBigInteger();
                        // 这里要用合约创建地址执行，不然没有权限
                        TransactionOpts mintOpts = new TransactionOpts();
                        mintOpts.setFrom(creator.getAddress());
                        mintOpts.setSigner(accountMgr);
                        System.out.printf("正在给地址 (%s) 铸造增发，数量: %s%n", mintAddress, realMintValue);
                        Hash mintTransactionHash = sender.Mint(mintOpts, mintAddress, realMintValue);
                        // 订阅交易确认消息
                        TransactionObserver mintTransferObserver = synchronizer.subscribeTransactionConfirm(
                                mintTransactionHash, Common.DEFAULT_CONFIRM_COUNT);
                        // 等待交易确认
                        mintTransferObserver.waitForNotify();
                        // 获取回执结果，判定是否执行成功
                        TransactionReceipt mintReceipt = chainService.getReceiptByHash(mintTransactionHash);
                        Integer mintReceiptStatus = mintReceipt.getStatus();
                        if (mintReceiptStatus.equals(1)){
                            System.out.println("铸造增发成功！！！");
                        }else{
                            System.out.println("铸造增发失败！！！");
                        }
                        break;
                    case "newAccount":
                        // 新建账户
                        Address newAddress = accountMgr.newAccount();
                        System.out.println(newAddress.toBase58());
                        break;
                    case "importAccount":
                        // 导入私钥
                        if(options.length < 1){
                            usage();
                            break;
                        }
                        Address importAddress = accountMgr.importAccount(options[1]);
                        System.out.println(importAddress.toBase58());
                        break;
                    case "balanceOf":
                        if (options.length < 1) {
                            usage();
                            break;
                        }
                        Address balanceOfAddress = Address.fromString(options[0]);
                        BigInteger gotBalance = caller.BalanceOf(null, balanceOfAddress);
                        // 真实的余额是要除以小数精度的，这里保留6位小数
                        BigDecimal gotBalanceN = gotBalance==null?BigDecimal.ZERO:
                                new BigDecimal(gotBalance).divide(BigDecimal.TEN.pow(USDTCoin.DECIMALS),
                                        6, RoundingMode.DOWN);
                        System.out.printf("%s%n", gotBalanceN);
                        break;
                    case "accounts":
                        // 输出账户列表
                        Map<Address, Account> accountMap = accountMgr.getAccountMap();
                        if (accountMap.isEmpty()) {
                            System.out.println("无数据");
                            break;
                        }
                        System.out.printf("%-30s %-17s", "账户地址", "余额");
                        System.out.println();
                        for (Address address : accountMap.keySet()) {
                            String addressString = address.toBase58();
                            System.out.printf("%-33s ", addressString);
                            // 获取账户余额
                            BigInteger balance = accountMgr.getBalance(address);
                            // 真实的余额是要除以小数精度的，这里保留6位小数
                            BigDecimal balanceN = balance==null?BigDecimal.ZERO:
                                    new BigDecimal(balance).divide(BigDecimal.TEN.pow(USDTCoin.DECIMALS),
                                            6, RoundingMode.DOWN);
                            System.out.printf("%-18s ", balanceN);
                            System.out.println();
                        }
                        break;
                    case "transfer":
                        if (options.length < 3) {
                            usage();
                            break;
                        }
                        Address from = Address.fromString(options[0]);
                        Address to = Address.fromString(options[1]);
                        BigDecimal transferValue = new BigDecimal(options[2]);
                        TransactionOpts opts = new TransactionOpts();
                        opts.setFrom(from);
                        opts.setSigner(accountMgr);
                        System.out.printf("正在从地址 (%s) 向地址 (%s) 转移代币，数量: %s%n", from, to, transferValue);
                        // 这里要乘以小数精度，因为用户输入的是带小数的并且给用户展示的也是除以了小数精度的
                        BigInteger realTransferValueValue = transferValue.multiply(
                                BigDecimal.TEN.pow(USDTCoin.DECIMALS))
                                .toBigInteger();
                        Hash txHash = sender.Transfer(opts, to, realTransferValueValue);
                        System.out.printf("发送转移消息成功，交易消息（%s）等待上链确认，当前确认次数: %d%n", txHash, Common.DEFAULT_CONFIRM_COUNT);
                        // 订阅交易确认消息
                        TransactionObserver transferObserver = synchronizer.subscribeTransactionConfirm(
                                txHash, Common.DEFAULT_CONFIRM_COUNT);
                        // 等待交易确认
                        transferObserver.waitForNotify();
                        // 获取回执结果，判定是否执行成功
                        TransactionReceipt transactionReceipt = chainService.getReceiptByHash(txHash);
                        Integer transactionReceiptStatus = transactionReceipt.getStatus();
                        if (transactionReceiptStatus.equals(1)){
                            System.out.println("转移成功！！！");
                        }else{
                            System.out.println("转移失败！！！");
                        }
                        break;
                    case "transferFrom":
                        if (options.length < 4) {
                            usage();
                            break;
                        }
                        Address transferSpenderAddress = Address.fromString(options[0]);
                        Address transferFromAddress = Address.fromString(options[1]);
                        Address transferToAddress = Address.fromString(options[2]);
                        BigDecimal transferFromValue = new BigDecimal(options[3]);
                        TransactionOpts transferFromOpts = new TransactionOpts();
                        transferFromOpts.setFrom(transferSpenderAddress);
                        transferFromOpts.setSigner(accountMgr);
                        System.out.printf("正在使用 %s 地址从 (%s) 地址向 (%s) 地址转移代币，数量: %s%n",
                                transferSpenderAddress, transferFromAddress, transferToAddress, transferFromValue);
                        // 这里要乘以小数精度，因为用户输入的是带小数的并且给用户展示的也是除以了小数精度的
                        BigInteger realTransferFromValueValue = transferFromValue.multiply(
                                        BigDecimal.TEN.pow(USDTCoin.DECIMALS))
                                .toBigInteger();
                        Hash transferFromHash = sender.TransferFrom(
                                transferFromOpts, transferFromAddress , transferToAddress,
                                realTransferFromValueValue);
                        System.out.printf("发送转移消息成功，交易消息（%s）等待上链确认，当前确认次数: %d%n",
                                transferFromHash, Common.DEFAULT_CONFIRM_COUNT);
                        // 订阅交易确认消息
                        TransactionObserver transferFromObserver = synchronizer.subscribeTransactionConfirm(
                                transferFromHash, Common.DEFAULT_CONFIRM_COUNT);
                        // 等待交易确认
                        transferFromObserver.waitForNotify();
                        // 获取回执结果，判定是否执行成功
                        TransactionReceipt receipt = chainService.getReceiptByHash(transferFromHash);
                        Integer status = receipt.getStatus();
                        if (status.equals(1)){
                            System.out.println("转移成功！！！");
                        }else{
                            System.out.println("转移失败！！！");
                        }
                        break;
                }
            } catch (Exception e) {
                System.out.printf("错误：%s", e.getMessage());
                System.out.println();
            }
        }
        timer.cancel();
    }
}
