package com.admin.controller;

import com.admin.domain.ResponseResult;
import com.admin.domain.entity.MerchantBean;
import com.admin.domain.vo.MerchantVo;
import com.admin.service.MerchantService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Xqf
 * @version 1.0
 */
@RestController("/merchant")
@Api("商户控制器")
public class MerchantController {

    @Autowired
    private MerchantService merchantService;

    @GetMapping("/merchantList")
    @ApiOperation(value = "商户列表")
    public ResponseResult getMerchantList() {


        return ResponseResult.okResult(merchantService.getMerchantList());
    }
    @PostMapping("/updateMerchant")
    @ApiOperation("更新商户")
    public ResponseResult updateMerchant(@RequestBody MerchantVo merchantVo){

        return merchantService.insertMerchant(merchantVo);
    }

}
