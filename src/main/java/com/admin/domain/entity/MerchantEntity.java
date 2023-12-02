package com.admin.domain.entity;

import com.admin.dao.MerchantRepository;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

@Data
@Document(collection = "tb_merchant")
@EqualsAndHashCode(callSuper = false)
public class MerchantEntity {
    @Id
    @NotNull
    @javax.persistence.Id
    Long id;
    /**
     * 商户名称
     */
    @NotNull
    @Field(name = "name")
    String merchantName;
    /**
     * QQ号
     */
    @NotNull
    @Column(name = "qq")
    long qq;
    /**
     * 微信账号
     */
    @NotNull
    @Column(name = "wx")
    String wx;
    /**
     * 歪歪账号
     */
    @NotNull
    @Column(name = "yy")
    String yy;
    /**
     * 提现比例(游戏币比例)
     */
    @NotNull
    @Column(name = "ratio")
    int ratio;
    /**
     * 商户金币
     */
    @Column(name = "gold")
    Long gold;
    /**
     * 渠道号
     */
    @Column(name = "channel")
    int channel;

}
