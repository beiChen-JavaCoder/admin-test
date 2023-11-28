package com.admin.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * 玩家流水实体
 * @author Xqf
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("tb_coinlog")
public class CoinLog {

    /**
     * 流水id
     */
    @Id
    private String id;
    /**
     * 玩家id
     */
    private Long rid;

    private Long black_white;

    @Field("rolename")
    private String roleName;
    /**
     * 起始分数
     */
    @Field("base_score")
    private Long baseScore;

    /**
     * 游戏id
     */
    @Field("gameid")
    private Long gameId;
    /**
     *
     */
    private String key;
    /**
     * 创建时间
     */
    @Field("time_stamp")
    private Long timeStamp;
    /**
     * 输或赢
     */
    private Long num;
    /**
     * 变动前
     */
    @Field("aftertotal")
    private Long afterTotal;
    /**
     * 变动后
     */
    @Field("beforetotal")
    private Long beforeTotal;
}

