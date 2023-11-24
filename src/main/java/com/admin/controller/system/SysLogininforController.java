package com.admin.controller.system;

import com.admin.annotation.Log;
import com.admin.domain.ResponseResult;
import com.admin.domain.entity.SysLogininfor;
import com.admin.domain.vo.QueryParamsVo;
import com.admin.enums.BusinessType;
import com.admin.service.SysLogininforService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Xqf
 * @version 1.0
 */
@Api("登录日志模块")
@RequestMapping("/system/logininfor")
@RestController
public class SysLogininforController {

    @Autowired
    private SysLogininforService logininforService;

    @ApiOperation("日志列表")
    @PostMapping("/list")
    public ResponseResult list(@RequestBody QueryParamsVo queryParamsVo) {

        return logininforService.findLogininforPage(queryParamsVo);
    }

    @Log(title = "登录日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/{infoIds}")
    public ResponseResult remove(@PathVariable Long[] infoIds) {
        return logininforService.deleteLogininforByIds(infoIds);
    }

//    @PreAuthorize("@ss.hasPermi('monitor:logininfor:remove')")
    @Log(title = "登录日志", businessType = BusinessType.CLEAN)
    @DeleteMapping("/clean")
    public ResponseResult clean() {

        return logininforService.cleanLogininfor();
    }

//    @PreAuthorize("@ss.hasPermi('monitor:logininfor:unlock')")
//    @Log(title = "账户解锁", businessType = BusinessType.OTHER)
//    @GetMapping("/unlock/{userName}")
//    public ResponseResult unlock(@PathVariable("userName") String userName) {
//        return passwordService.clearLoginRecordCache(userName);
//    }


}
