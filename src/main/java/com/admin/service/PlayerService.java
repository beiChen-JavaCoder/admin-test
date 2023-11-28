package com.admin.service;

import com.admin.domain.ResponseResult;
import com.admin.domain.vo.*;

/**
 * @author Xqf
 * @version 1.0
 */
public interface PlayerService {

    /**
     * 商户拉取玩家充值列表
     * @return
     */
     ResponseResult<PageVo> findPlayerRechargePage(PlayerRechargeVo playerRechargeVo,Integer pageNum,Integer pageSize);

    /**
     * 商户对玩家进行充值
     * @return
     */
     ResponseResult updateRoleGold(RechargeVo rechargeVo);

    /**
     * 玩家列表
     * @param queryParamsVo
     * @return
     */
    ResponseResult findPlayerPage(PlayerVo queryParamsVo);

    /**
     * 玩家流水列表
     * @return
     */
    ResponseResult findFlowPage(QueryParamsVo queryParamsVo);

    /**
     * 税收列表
     * @param revenueVo
     * @return
     */
    ResponseResult findTallagePage(RevenueVo revenueVo);
}
