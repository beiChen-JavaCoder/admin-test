package com.admin.service.Imp;

import cn.hutool.core.date.DateUtil;
import com.admin.domain.ResponseResult;
import com.admin.domain.entity.MerchantBean;
import com.admin.domain.entity.MerchantOrderEntity;
import com.admin.domain.dto.MerchantDto;
import com.admin.domain.vo.MerchantVo;
import com.admin.domain.vo.PageVo;
import com.admin.enums.AppHttpCodeEnum;
import com.admin.enums.MerchantTypeEnum;
import com.admin.service.MerchantService;
import com.admin.utils.BeanCopyUtils;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.chart.PieChart;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.sql.Date;
import java.util.List;

/**
 * @author Xqf
 * @version 1.0
 */
@Slf4j
@Service
public class MerchantServiceImp implements MerchantService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public PageVo getMerchantList() {

        List<MerchantBean> Merchants = mongoTemplate.findAll(MerchantBean.class);
        PageVo pageVo = new PageVo();
        pageVo.setRows(Merchants);
        pageVo.setTotal(Long.getLong(String.valueOf(Merchants.size())));

        return pageVo;
    }

    @Override
    public ResponseResult insertMerchant(MerchantVo merchantVo) {

        MerchantOrderEntity merchantOrder = new MerchantOrderEntity();
        merchantOrder.setNum(merchantVo.getNum());
        merchantOrder.setRid(merchantVo.getId());
        merchantOrder.setCreateTime(DateUtil.date());
        merchantOrder.setPayeeAccount(merchantOrder.getPayeeAccount());
        merchantOrder.setPayeeNickname(merchantVo.getName());
        merchantOrder.setMerchantId(merchantVo.getId());

        PageVo pageVo = new PageVo();
        MerchantOrderEntity reMerchantOrder = mongoTemplate.insert(merchantOrder);
        if (merchantOrder != null) {
            MerchantDto merchantDto = new MerchantDto();
            //todo 封装提现的用户对象
            merchantDto.setUserId(new Long("1111"));
            merchantDto.setChangeNum(merchantVo.getNum());
            merchantDto.setType(MerchantTypeEnum.CASH.getType());
            notificationMerchant(merchantDto);

            return ResponseResult.okResult(AppHttpCodeEnum.MERCHANT_ORDER_OK.getMsg());
        } else {
            log.error("订单生成失败");
            return ResponseResult.errorResult(AppHttpCodeEnum.MERCHANT_ORDER_NO);
        }



    }

    private Mono<String> notificationMerchant(MerchantDto merchantDto) {
        // 将 MerchantBean 对象转换为 JSON 字符串
        String jsonString = JSON.toJSONString(merchantDto);


        WebClient webClient = WebClient.create("http://192.168.10.62:9998");
        Mono<String> result = webClient.post()
                .uri("/hall/merchant/merchantListChange")
                .contentType(MediaType.APPLICATION_JSON)  // 设置请求体的内容类型
                .body(BodyInserters.fromValue(jsonString))  // 设置请求体的内容
                .retrieve()
                .bodyToMono(String.class);

        return result;
    }
}
