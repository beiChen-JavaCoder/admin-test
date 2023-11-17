package com.admin.service;

import com.admin.domain.ResponseResult;
import com.admin.domain.vo.PageVo;
import com.admin.domain.vo.QueryParamsVo;
import com.admin.domain.vo.RechargeVo;
import com.admin.domain.vo.PlayerRechargeVo;

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
    ResponseResult findPlayerPage(QueryParamsVo queryParamsVo);


}
