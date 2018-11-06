package cn.com.crv.blockchain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Transaction {
    /**
     * 交易唯一标识
     */
    private int id;
    /**
     * 发起交易方钱包地址
     */
    private String fromWallet;
    /**
     * 交易接收方钱包地址
     */
    private String toWallet;
    /**
     * 交易金额
     */
    private double value;
    /**
     * 手续费
     */
    private double poundage;

}
