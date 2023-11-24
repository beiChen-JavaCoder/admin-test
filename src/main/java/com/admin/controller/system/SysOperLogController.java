package com.admin.controller.system;

import com.admin.annotation.Log;
import com.admin.domain.ResponseResult;
import com.admin.domain.vo.QueryParamsVo;
import com.admin.enums.BusinessType;
import com.admin.service.SysOperLogService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Xqf
 * 操作日志控制器
 * @version 1.0
 */
@RestController
@RequestMapping("/system/operlog")
public class SysOperLogController {

    @Autowired
    private SysOperLogService operLogService;

    @ApiOperation("操作日志列表")
    @PostMapping("list")
    public ResponseResult list(@RequestBody QueryParamsVo queryParamsVo) {

        return operLogService.findPageOperLog(queryParamsVo);


    }

    @Log(title = "操作日志", businessType = BusinessType.CLEAN)
    @ApiOperation("清空操作日志")
    @DeleteMapping("clean")
    public ResponseResult clean(){

        return operLogService.cleanOperLog();
    }
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/{operIds}")
    public ResponseResult remove(@PathVariable Long[] operIds)
    {
        return operLogService.deleteOperLogByIds(operIds);
    }
}
