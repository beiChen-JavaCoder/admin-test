package com.admin.domain.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Column;

@Data
@Document(collection = "tb_merchant")
@EqualsAndHashCode(callSuper = false)
public class MerchantEntity {
    @Id
    @javax.persistence.Id
    Long id;
    /**
     * 商户名称
     */
    @Column(name = "name")
    String name;
    /**
     * QQ号
     */
    @Column(name = "qq")
    long qq;
    /**
     * 微信账号
     */
    @Column(name = "wx")
    String wx;
    /**
     * 歪歪账号
     */
    @Column(name = "yy")
    String yy;
    /**
     * 提现比例(游戏币比例)
     */
    @Column(name = "ratio")
    int ratio;
}