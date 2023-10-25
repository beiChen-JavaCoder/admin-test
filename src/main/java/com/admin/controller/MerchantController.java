package com.admin.controller;

import com.admin.domain.ResponseResult;
import com.admin.domain.entity.MerchantEntity;
import com.admin.domain.vo.PageVo;
import com.admin.service.MerchantService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author Xqf
 * @version 1.0
 */
@RestController
@Api("商户控制器")
public class MerchantController {

    @Autowired
    private MerchantService merchantService;
    @Autowired
    private MongoTemplate mongoTemplate;

    @GetMapping("/merchantList")
    public ResponseResult getMerchantList(){

        Mono<String> merchantList = merchantService.getMerchantList();
        List<MerchantEntity> all = mongoTemplate.findAll(MerchantEntity.class);
        PageVo pageVo = new PageVo();
        pageVo.setRows(all);
        pageVo.setTotal(Long.getLong(String.valueOf(all.size())));
        return ResponseResult.okResult(pageVo);
    }

}
