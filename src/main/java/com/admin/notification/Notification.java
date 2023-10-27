package com.admin.notification;

import com.admin.domain.dto.MerchantDto;
import com.alibaba.fastjson.JSON;
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

    public static Mono<String> notificationMerchant(MerchantDto merchantDto) {
        // 将 MerchantBean 对象转换为 JSON 字符串
        String jsonString = JSON.toJSONString(merchantDto);

        WebClient webClient = WebClient.create("http://192.168.10.62:9998");
        Mono<String> stringMono = webClient.post()
                .uri("/hall/merchant/merchantListChange")
                .contentType(MediaType.APPLICATION_JSON)  // 设置请求体的内容类型
                .body(BodyInserters.fromValue(jsonString))  // 设置请求体的内容
                .retrieve()
                .bodyToMono(String.class);
        log.info("游戏服返回的结果:"+JSON.toJSONString(stringMono));
        return stringMono ;


    }
}
