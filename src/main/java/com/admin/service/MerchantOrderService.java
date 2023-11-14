package com.admin.service;

import com.admin.domain.ResponseResult;
import com.admin.domain.entity.MerchantOrderEntity;
import com.admin.domain.vo.MerchantOrderVo;
import com.admin.domain.vo.PageVo;

/**
 * @author Xqf
 * @version 1.0
 */

public interface MerchantOrderService {
    /**
     * 根据查询条件查询提现审核列表
     * @param merchantOrderVo
     * @return
     */
    ResponseResult<PageVo> findOrderPage(MerchantOrderVo merchantOrderVo);

    /**
     * 提现审核
     * @param merchantOrder
     * @return
     */
    ResponseResult update(MerchantOrderEntity merchantOrder);

}
