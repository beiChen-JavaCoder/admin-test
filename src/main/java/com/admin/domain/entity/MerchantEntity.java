package com.admin.domain.entity;

import com.admin.dao.MerchantRepository;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

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
    @Field(name = "name")
    String merchantName;
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
    /**
     * 商户金币
     */
    @Column(name = "gold")
    Long gold;
    /**
     * 渠道
     */
    @Column(name = "channel")
    Long channel;

}
