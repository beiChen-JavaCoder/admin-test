package com.admin.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

/**
 * 玩家流水Vo
 *
 * @author Xqf
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlowVo {

    /**
     * 流水id
     */
    private String id;
    /**
     * 玩家id
     */
    private Long rid;



    private String roleName;
    /**
     * 起始分数
     */

    private Long baseScore;

    /**
     * 游戏id
     */

    private Long gameId;

    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 输或赢
     */
    private Long num;
    /**
     * 变动前
     */
    private Long afterTotal;
    /**
     * 变动后
     */
    private Long beforeTotal;
}
