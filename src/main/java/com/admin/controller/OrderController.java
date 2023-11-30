package com.admin.controller;

import com.admin.annotation.Log;
import com.admin.domain.ResponseResult;
import com.admin.domain.entity.MerchantOrderEntity;
import com.admin.domain.vo.MerchantOrderVo;
import com.admin.domain.vo.PageVo;
import com.admin.enums.BusinessType;
import com.admin.service.MerchantOrderService;
import com.admin.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Check;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Xqf
 * @version 1.0
 */
@Api("订单模块")
@RestController
@RequestMapping("/order")
@Slf4j
@PreAuthorize("@ss.hasRole('merchant')")
public class OrderController {

    @Autowired
    private MerchantOrderService merchantOrderService;

    @ApiOperation("订单列表")
    @GetMapping("/list")
    public ResponseResult<PageVo> list(MerchantOrderVo merchantOrderVo) {

        return merchantOrderService.findOrderPage(merchantOrderVo);

    }

    @Log(title = "订单审核", businessType = BusinessType.UPDATE)
    @ApiOperation("订单审核")
    @PutMapping
    public ResponseResult update(@RequestBody MerchantOrderEntity merchantOrder) {

        //校验凭证不能为空
        if (!checkParameter(merchantOrder)) {
            Long userId = SecurityUtils.getUserId();
            log.error("用户id:" + userId + "尝试审核提现订单:" + merchantOrder.getId());
            return ResponseResult.errorResult(500, "凭证不能为空,请上传凭证");
        }
        return merchantOrderService.update(merchantOrder);


    }

    private boolean checkParameter(Object object) {

        return object != null && ((MerchantOrderEntity) object).getVoucher() != null;


    }


}
