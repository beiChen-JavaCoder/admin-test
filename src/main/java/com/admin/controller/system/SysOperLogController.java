package com.admin.controller.system;

import com.admin.domain.ResponseResult;
import com.admin.domain.vo.QueryParamsVo;
import com.admin.service.SysOperLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Xqf
 * @version 1.0
 */
@RestController
@RequestMapping("/system/operlog")
public class SysOperLogController {

    @Autowired
    private SysOperLogService operLogService;


    @PostMapping("list")
    public ResponseResult list(@RequestBody QueryParamsVo queryParamsVo) {

        return operLogService.findPageOperLog(queryParamsVo);


    }
}
