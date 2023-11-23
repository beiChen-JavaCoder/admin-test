package com.admin.service;

import com.admin.domain.ResponseResult;
import com.admin.domain.entity.SysLogininfor;
import com.admin.domain.entity.SysOperLog;
import com.admin.domain.vo.QueryParamsVo;

/**
 * 登录日志 服务层
 *
 * @author ruoyi
 */
public interface SysLogininforService {
    ResponseResult findLogininforPage(QueryParamsVo queryParamsVo);


    /**
     * 新增系统登录日志
     *
     * @param logininfor 访问日志对象
     */
    public void insertLogininfor(SysLogininfor logininfor);





}
