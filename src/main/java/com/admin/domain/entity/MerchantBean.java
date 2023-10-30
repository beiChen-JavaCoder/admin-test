package com.admin.domain.entity;

import lombok.Data;

/**
 * 商户http请求(商户列表变更请求地址:/hall/merchant/merchantListChange,充值请求地址:/hall/merchant/recharge,提现请求地址:/hall/merchant/cash)
 */
@Data
public class MerchantBean {
    Long id;
    /** 操作类型:1.商户列表变更,2.充值,3.提现 */
    int type;
    /** 提现订单编号(仅提现时使用) */
    long oderId;
    /** 商户编号 */
    int merchantId;
    /** 玩家编号 */
    long userId;
    /** 变更金额 */
    long changeNum;
}