package com.admin.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Xqf
 * @version 1.0
 * 充值vo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RechargeVo {
    /**
     * 商户id
     */
    private Long merchantId;
    /**
     * 玩家id
     */

    private Integer rid;
    /**
     * 用户名称
     */
    private String userName;
    /**
     * 玩家昵称
     */
    private String rolename;
    /**
     * 修改金币数量
     */
    private Integer num;


}
