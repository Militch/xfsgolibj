package tech.xfs.xfsgolibj.common;

import com.google.gson.annotations.SerializedName;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public class TransactionReceipt {
    @SerializedName("version")
    private Integer version;
    @SerializedName("status")
    private Integer status;
    @SerializedName("tx_hash")
    private Hash txHash;
    @SerializedName("block_hash")
    private Hash blockHash;
    @SerializedName("block_index")
    private Long blockIndex;
    @SerializedName("block_height")
    private Long blockHeight;

    @SerializedName("tx_index")
    private Long txIndex;
    @SerializedName("logs")
    private List<Hash> logs;
    @SerializedName("gas_used")
    private BigInteger gasUsed;

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Hash getTxHash() {
        return txHash;
    }

    public void setTxHash(Hash txHash) {
        this.txHash = txHash;
    }

    public Hash getBlockHash() {
        return blockHash;
    }

    public void setBlockHash(Hash blockHash) {
        this.blockHash = blockHash;
    }

    public Long getBlockIndex() {
        return blockIndex;
    }

    public void setBlockIndex(Long blockIndex) {
        this.blockIndex = blockIndex;
    }

    public BigInteger getGasUsed() {
        return gasUsed;
    }

    public void setGasUsed(BigInteger gasUsed) {
        this.gasUsed = gasUsed;
    }

    public Long getBlockHeight() {
        return blockHeight;
    }

    public void setBlockHeight(Long blockHeight) {
        this.blockHeight = blockHeight;
    }

    public Long getTxIndex() {
        return txIndex;
    }

    public void setTxIndex(Long txIndex) {
        this.txIndex = txIndex;
    }

    public List<Hash> getLogs() {
        return logs;
    }

    public void setLogs(List<Hash> logs) {
        this.logs = logs;
    }
}
