package com.admin.service;

import com.admin.domain.ResponseResult;
import com.admin.domain.vo.MerchantVo;
import com.admin.domain.vo.PageVo;

import java.util.List;

/**
 * @author Xqf
 * @version 1.0
 */
public interface MerchantService {
    /**
     * 商户列表页
     *
     * @return
     */
    ResponseResult<PageVo> findMerchantPage(MerchantVo merchantVo, Integer pageNum, Integer pageSize) ;

    ResponseResult insertMerchant(MerchantVo merchantVo);

    ResponseResult<PageVo> removeMerchantById(List<Long> ids);
}
