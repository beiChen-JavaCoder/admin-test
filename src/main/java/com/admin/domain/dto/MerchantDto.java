package com.admin.domain.dto;

import lombok.Data;

@Data
public class MerchantDto {
    /** 操作类型:1.商户列表变更,2.充值,3.提现 */
    Integer type;
    /** 商户编号 */
    int merchantId;
    /** 玩家编号 */
    long userId;
    /** 变更金额 */
    long changeNum;
}