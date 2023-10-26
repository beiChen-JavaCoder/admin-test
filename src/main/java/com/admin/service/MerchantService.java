package com.admin.service;

import com.admin.domain.ResponseResult;
import com.admin.domain.entity.MerchantBean;
import com.admin.domain.vo.MerchantVo;
import com.admin.domain.vo.PageVo;

/**
 * @author Xqf
 * @version 1.0
 */
public interface MerchantService {
    /**
     * 商户列表页
     * @return
     */
    PageVo getMerchantList() ;

    ResponseResult insertMerchant(MerchantVo merchantVo);
}
