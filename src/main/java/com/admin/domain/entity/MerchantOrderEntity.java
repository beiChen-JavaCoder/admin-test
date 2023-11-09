package com.admin.domain.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Date;

/**
 * 商户订单
 */
@Data
@Document(collection = "tb_merchant_order")
@EqualsAndHashCode(callSuper = false)
public class MerchantOrderEntity implements Serializable {
    @Id
    @javax.persistence.Id
    long id;
    /**
     * 玩家编号
     */
    @Column(name = "rid")
    long rid;
    /**
     * 商户编号
     */
    @Column(name = "merchant_id")
    long merchantId;
    /**
     * 提现的游戏币数量
     */
    @Column(name = "num")
    long num;
    /**
     * 账号类型(提现渠道:1.微信,2.支付宝,3.银行卡)
     */
    @Column(name = "account_type")
    int accountType;

    /**
     * 收款账号
     */
    @Column(name = "payee_account")
    String payeeAccount;
    /**
     * 收款昵称
     */
    @Column(name = "payee_nickname")
    String payeeNickname;
    /**
     * 订单状态(0.待处理,1.已处理,2.已拒绝,3.处理超时)
     */
    @Column(name = "status")
    int status;
    /**
     * 交易凭证(仅在订单为已处理状态时使用)
     */
    @Column(name = "voucher")
    String voucher;
    /**
     * 订单描述
     */
    @Column(name = "remarks")
    String remarks;
    /**
     * 订单创建时间
     */
    @Column(name = "create_time")
    Long createTime;
    /**
     * 处理超时时间(到达此时间数据未处理则进入废弃处理流程)
     */
    @Column(name = "time_out")
    long timeOut;
    /**
     * 用于前端转换
     */
    @Transient
    String sAccountType;
    @Transient
    String sStatus;
    @Transient
    String createTimeDate;
    @Transient
    String timeOutDate;

}