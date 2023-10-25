package com.admin.service;

import reactor.core.publisher.Mono;

/**
 * @author Xqf
 * @version 1.0
 */
public interface MerchantService {
    Mono<String> getMerchantList();
}
