package com.admin.domain.vo;

import lombok.Data;

import javax.persistence.Column;

/**
 * @author Xqf
 * @version 1.0
 */
@Data
public class MerchantOrderVo {

    /**
     * 玩家编号
     */
    private Long rid;
    /**
     * 交易凭证(仅在订单为已处理状态时使用)
     */
    private String voucher;
    /**
     * 分页数量
     */
    private Integer pageNum;
    /**
     * 分页大小
     */
    private Integer pageSize;

}
