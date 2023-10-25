package com.admin.service.Imp;

import com.admin.service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author Xqf
 * @version 1.0
 */
@Service
public class MerchantServiceImp implements MerchantService {


    @Override
    public Mono<String> getMerchantList() {
        WebClient webClient = WebClient.create("http://example.com");
        Mono<String> result = webClient.get()
                .uri("/hall/merchant/mercantListChange")
                .retrieve()
                .bodyToMono(String.class);

        return result;
    }
}
