package com.admin.service;

import cn.hutool.extra.spring.SpringUtil;
import com.admin.domain.entity.MerchantBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Xqf
 * @version 1.0
 */
@Component
public class MerchantIdManager {
    AtomicLong maxMerchantId = new AtomicLong();
    @Autowired
    MongoTemplate mongoTemplate;
//    private static MerchantIdManager INSTANCE;
//
//    public static MerchantIdManager getInstance() {
//        if (INSTANCE == null) {
//            INSTANCE = SpringUtil.getBean(MerchantIdManager.class);
//        }
//        return INSTANCE;
//    }

//    MerchantIdManager() {
//        init();
////        INSTANCE = this;
//    }

    public AtomicLong getMaxMerchantId() {
        return maxMerchantId;
    }

    @PostConstruct
    void init() {
        Query query = new Query().with(Sort.by(Sort.Direction.DESC, "_id")).limit(1);
        MerchantBean merchant = mongoTemplate.findOne(query, MerchantBean.class);
        if (merchant != null) {
            maxMerchantId.set(merchant.getId());
        }
    }
}
