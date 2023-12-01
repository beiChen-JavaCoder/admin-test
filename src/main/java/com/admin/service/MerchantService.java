package com.admin.service;

import com.admin.dao.MerchantRepository;
import com.admin.domain.ResponseResult;
import com.admin.domain.entity.MerchantEntity;
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

    /**
     * 新增商户
     * @param merchantEntity
     * @return
     */
    ResponseResult addMerchant(MerchantEntity merchantEntity);

    /**
     * 根据id删除商户
     * @param ids 可为集合
     * @return
     */
    ResponseResult<PageVo> removeMerchantById(List<Long> ids);

    /**
     * 根据用户id查询商户
     * @param userId
     * @return
     */
    MerchantEntity findMerchantByUserId(Long userId);



    ResponseResult findMerchantByUserId();

    ResponseResult updateMerchantByid(MerchantEntity merchant);

    /**
     * 通过id查商户
     * @param merchantId
     * @return
     */
    MerchantEntity findMerchantById(Long merchantId);
}
