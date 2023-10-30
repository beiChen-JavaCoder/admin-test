package com.admin.service;

import com.admin.domain.ResponseResult;
import com.admin.domain.vo.PageVo;
import com.admin.domain.vo.RechargeVo;
import com.admin.domain.vo.RoleInfoVo;

/**
 * @author Xqf
 * @version 1.0
 */
public interface RoleInfoService {
    ResponseResult<PageVo> findRolePage(RoleInfoVo roleInfoVo, Integer pageNum, Integer pageSize);

    ResponseResult updateRoleGold(RechargeVo rechargeVo);

}
