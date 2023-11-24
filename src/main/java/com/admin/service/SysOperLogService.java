package com.admin.service;

import com.admin.domain.ResponseResult;
import com.admin.domain.entity.SysOperLog;
import com.admin.domain.vo.QueryParamsVo;

/**
 * 操作日志 服务层
 *
 * @author ruoyi
 */
public interface SysOperLogService {

    /**
     * 新增操作日志
     *
     * @param operLog 操作日志对象
     */
    public void insertOperlog(SysOperLog operLog);


    ResponseResult findPageOperLog(QueryParamsVo queryParamsVo);

    ResponseResult cleanOperLog();

    ResponseResult deleteOperLogByIds(Long[] operIds);
}
