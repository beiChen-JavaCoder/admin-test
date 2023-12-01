package com.admin.controller;

import com.admin.annotation.Log;
import com.admin.dao.MerchantRepository;
import com.admin.domain.ResponseResult;
import com.admin.domain.entity.MerchantEntity;
import com.admin.domain.vo.MerchantVo;
import com.admin.domain.vo.PageVo;
import com.admin.domain.vo.RevenueVo;
import com.admin.enums.AppHttpCodeEnum;
import com.admin.enums.BusinessType;
import com.admin.service.MerchantService;
import com.admin.utils.BeanCopyUtils;
import com.admin.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * @author Xqf
 * @version 1.0
 */
@RestController
@RequestMapping("/merchant")
@PreAuthorize("@ss.hasRole('merchant')")
@Api("商户模块")
public class MerchantController {

    @Autowired
    private MerchantService merchantService;


    @GetMapping("/merchantList")
    @ApiOperation(value = "商户列表")
    public ResponseResult<PageVo> getMerchantList(MerchantVo merchantVo, Integer pageNum, Integer pageSize) {


        return merchantService.findMerchantPage(merchantVo, pageNum, pageSize);
    }
    @Log(title = "删除商户",businessType = BusinessType.DELETE)
    @DeleteMapping("/{merchantIds}")
    @ApiOperation(value = "删除商户")
    public ResponseResult delMerchant(@PathVariable List<Long> merchantIds) {
//        if (merchantIds.contains(SecurityUtils.getUserId())) {
//            return ResponseResult.errorResult(500, "不能删除当前你正在使用的用户所绑定的商户");
//        }
        merchantService.removeMerchantById(merchantIds);
        return ResponseResult.okResult();
    }

    @Log(title = "新增商户",businessType = BusinessType.INSERT)
    @PostMapping()
    @ApiOperation("新增商户")
    public ResponseResult addMerchant(@RequestBody MerchantVo merchantVo) {

        MerchantEntity merchantEntity = BeanCopyUtils.copyBean(merchantVo, MerchantEntity.class);
        return merchantService.addMerchant(merchantEntity);
    }
    @ApiOperation("获取商户税收百分比")
    @GetMapping("/merchantRatio")
    public ResponseResult getMerchantRatio() {
        return merchantService.findMerchantByUserId();
    }
    @Log(title = "删除商户",businessType = BusinessType.DELETE)
    @PutMapping()
    @ApiOperation(value = "修改商户信息")
    public ResponseResult updateMerchant( @RequestBody @Valid MerchantEntity merchant) {


        return merchantService.updateMerchantByid(merchant);
    }
}
