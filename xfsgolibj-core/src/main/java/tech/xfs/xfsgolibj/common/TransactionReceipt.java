package tech.xfs.xfsgolibj.common;

import com.google.gson.annotations.SerializedName;
import java.math.BigDecimal;

public class TransactionReceipt {
    @SerializedName("version")
    private Integer version;
    @SerializedName("status")
    private Integer status;
    @SerializedName("tx_hash")
    private String txHash;
    @SerializedName("block_hash")
    private String blockHash;
    @SerializedName("block_index")
    private Long blockIndex;
    @SerializedName("gas_used")
    private BigDecimal gasUsed;

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

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }

    public String getBlockHash() {
        return blockHash;
    }

    public void setBlockHash(String blockHash) {
        this.blockHash = blockHash;
    }

    public Long getBlockIndex() {
        return blockIndex;
    }

    public void setBlockIndex(Long blockIndex) {
        this.blockIndex = blockIndex;
    }

    public BigDecimal getGasUsed() {
        return gasUsed;
    }

    public void setGasUsed(BigDecimal gasUsed) {
        this.gasUsed = gasUsed;
    }
}
