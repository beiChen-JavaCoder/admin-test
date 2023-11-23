package com.admin.controller.system;

import com.admin.domain.ResponseResult;
import com.admin.domain.entity.SysLogininfor;
import com.admin.domain.vo.QueryParamsVo;
import com.admin.service.SysLogininforService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Xqf
 * @version 1.0
 */
@RequestMapping("/system/logininfor")
@RestController
public class SysLogininforController {

    @Autowired
    private SysLogininforService logininforService;


    @PostMapping("/list")
    public ResponseResult list(@RequestBody QueryParamsVo queryParamsVo) {

        return logininforService.findLogininforPage(queryParamsVo);
    }


}
