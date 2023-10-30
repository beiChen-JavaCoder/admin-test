package com.admin.notification;

import cn.hutool.http.HttpUtil;
import com.admin.domain.dto.MerchantDto;
import com.admin.enums.MerchantTypeEnum;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * @author Xqf
 * @version 1.0
 */
@Slf4j
public class Notification {
    /**
     * 变更列表url
     */
    private static final String MERCHANT_LIST_CHANGE = "http://192.168.10.62:9998/hall/merchant/merchantListChange";
    private static final String MERCHANT_RECHARGE_CHANGE = "http://192.168.10.62:9998/hall/merchant/recharge";
    private static final String MERCHANT_CASH_CHANGE = "http://192.168.10.62:9998//hall/merchant/cash";

    public static String notificationMerchant(MerchantDto merchantDto) {

        String url = null;


        // 将 MerchantBean 对象转换为 JSON 字符串
        String jsonString = JSON.toJSONString(merchantDto);

//        WebClient webClient = WebClient.create("http://192.168.10.62:9998");
//        Mono<String> stringMono = webClient.post()
//                .uri("/hall/merchant/merchantListChange")
//                .contentType(MediaType.APPLICATION_JSON)  // 设置请求体的内容类型
//                .body(BodyInserters.fromValue(jsonString))  // 设置请求体的内容
//                .retrieve()
//                .bodyToMono(String.class);
//        log.info("游戏服返回的结果:"+JSON.toJSONString(stringMono));
        if (merchantDto.getType() == MerchantTypeEnum.LIST.getType()) {
            url = MERCHANT_LIST_CHANGE;
        } else if (merchantDto.getType() == MerchantTypeEnum.CHARGE.getType()) {
            url = MERCHANT_RECHARGE_CHANGE;
        } else if (merchantDto.getType() == MerchantTypeEnum.CASH.getType()) {
            url = MERCHANT_CASH_CHANGE;
        }
        JSONObject parse = (JSONObject) JSON.parse(HttpUtil.post(url, jsonString));
        if (!"0".equals(parse.get("errcode"))) {
            return "1";
        }
        return "0";

    }
}
