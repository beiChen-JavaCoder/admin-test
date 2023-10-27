package com.admin.controller;

import com.admin.domain.ResponseResult;
import com.admin.domain.vo.MerchantVo;
import com.admin.domain.vo.PageVo;
import com.admin.enums.AppHttpCodeEnum;
import com.admin.service.MerchantService;
import com.admin.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Xqf
 * @version 1.0
 */
@RestController
@RequestMapping("/merchant")
@Api("商户控制器")
public class MerchantController {

    @Autowired
    private MerchantService merchantService;

    @GetMapping("/merchantList")
    @ApiOperation(value = "商户列表")
    public ResponseResult<PageVo> getMerchantList(MerchantVo merchantVo, Integer pageNum, Integer pageSize, String username) {


        return merchantService.findMerchantPage(merchantVo, pageNum, pageSize);
    }

    @DeleteMapping("/{merchantIds}")
    @ApiOperation(value = "删除商户")
    public ResponseResult delMerchant(@PathVariable List<String> merchantIds) {
        if (merchantIds.contains(SecurityUtils.getUserId())) {
            return ResponseResult.errorResult(500, "不能删除当前你正在使用的用户");
        }
        merchantService.removeMerchantById(merchantIds);
        return ResponseResult.okResult();
    }

    //    @PostMapping("/insertMerchant")
//    @ApiOperation("新增订单")
//    public ResponseResult<PageVo> insertMerchant(MerchantVo merchantVo) {
//        PageVo pageVo = merchantService.insertMerchant(merchantVo);
//        if (!pageVo.getRows().isEmpty()) {
//            return ResponseResult.okResult(pageVo);
//        } else {
//            return ResponseResult.errorResult(AppHttpCodeEnum.MERCHANT_ORDER_NO);
//        }
//    }
    @PostMapping()
    @ApiOperation("新增商户")
    public ResponseResult insertMerchant(@RequestBody MerchantVo merchantVo) {
        return merchantService.insertMerchant(merchantVo);
    }
}
