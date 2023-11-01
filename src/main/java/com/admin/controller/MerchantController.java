package com.admin.controller;

import com.admin.dao.MerchantRepository;
import com.admin.domain.ResponseResult;
import com.admin.domain.entity.MerchantEntity;
import com.admin.domain.vo.MerchantVo;
import com.admin.domain.vo.PageVo;
import com.admin.enums.AppHttpCodeEnum;
import com.admin.service.MerchantService;
import com.admin.utils.BeanCopyUtils;
import com.admin.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    @Autowired
    private MongoTemplate mongoTemplate;

    @GetMapping("/merchantList")
    @ApiOperation(value = "商户列表")
    public ResponseResult<PageVo> getMerchantList(MerchantVo merchantVo, Integer pageNum, Integer pageSize) {


        return merchantService.findMerchantPage(merchantVo, pageNum, pageSize);
    }

    @DeleteMapping("/{merchantIds}")
    @ApiOperation(value = "删除商户")
    public ResponseResult delMerchant(@PathVariable List<Long> merchantIds) {
//        if (merchantIds.contains(SecurityUtils.getUserId())) {
//            return ResponseResult.errorResult(500, "不能删除当前你正在使用的用户所绑定的商户");
//        }
        merchantService.removeMerchantById(merchantIds);
        return ResponseResult.okResult();
    }

    @PostMapping()
    @ApiOperation("新增商户")
    public ResponseResult addMerchant(@RequestBody MerchantVo merchantVo) {

        MerchantEntity merchantEntity = BeanCopyUtils.copyBean(merchantVo, MerchantEntity.class);

        return merchantService.addMerchant(merchantEntity);
    }
}
