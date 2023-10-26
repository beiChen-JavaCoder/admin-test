package com.admin.domain.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.persistence.Column;
import java.util.Date;

/**
 * 商户订单
 */
@Data
@Document(collection = "tb_merchant_order")
@EqualsAndHashCode(callSuper = false)
public class MerchantOrderEntity {
    @MongoId
    String id;
    /**
     * 玩家编号
     */
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
     * 订单创建时间
     */
    @Column(name = "create_time")
    Date createTime;
}