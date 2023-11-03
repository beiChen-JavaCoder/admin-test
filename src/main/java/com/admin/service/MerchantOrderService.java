package com.admin.service;

import com.admin.domain.ResponseResult;
import com.admin.domain.entity.MerchantOrderEntity;
import com.admin.domain.vo.MerchantOrderVo;
import com.admin.domain.vo.PageVo;

import java.util.List;

/**
 * @author Xqf
 * @version 1.0
 */

public interface MerchantOrderService {
    ResponseResult<PageVo> findOrderPage(MerchantOrderVo merchantOrderVo);
}
