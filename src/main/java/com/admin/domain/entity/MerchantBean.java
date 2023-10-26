package com.admin.domain.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;


@Data
@Document(collection = "tb_merchant")
@EqualsAndHashCode(callSuper = false)
public class MerchantBean {
    @MongoId
    Long id;
    /**
     * 商户名称
     */
    @Field("name")
    String name;
    /**
     * QQ号
     */
    @Field(name = "qq")
    long qq;
    /**
     * 微信账号
     */
    @Field(name = "wx")
    String wx;
    /**
     * 歪歪账号
     */
    @Field(name = "yy")
    String yy;
    /**
     * 提现比例(游戏币比例)
     */
    @Field(name = "ratio")
    int ratio;
}