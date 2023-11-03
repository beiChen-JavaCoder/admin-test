package com.admin.controller;

import com.admin.domain.ResponseResult;
import com.admin.domain.entity.MerchantOrderEntity;
import com.admin.domain.vo.MerchantOrderVo;
import com.admin.domain.vo.PageVo;
import com.admin.service.MerchantOrderService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Xqf
 * @version 1.0
 */
@Api("订单模块")
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private MerchantOrderService merchantOrderService;

    @GetMapping("/list")
    public ResponseResult<PageVo> list(MerchantOrderVo merchantOrderVo) {

        return merchantOrderService.findOrderPage(merchantOrderVo);

    }


}
