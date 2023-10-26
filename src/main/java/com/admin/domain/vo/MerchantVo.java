package com.admin.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

/**
 * @author Xqf
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MerchantVo {

    Long id;
    /**
     * 商户名称
     */

    String name;
    /**
     * QQ号
     */

    long qq;
    /**
     * 微信账号
     */

    String wx;
    /**
     * 歪歪账号
     */

    String yy;
    /**
     * 提现比例(游戏币比例)
     */

    int ratio;
    /**
     * 提现数量
     */
    int num;
    /**
     * 账户类型
     */
    String accountType;
}
