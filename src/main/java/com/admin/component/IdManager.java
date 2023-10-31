package com.admin.component;

import com.admin.domain.entity.MerchantEntity;
import com.admin.domain.entity.User;
import lombok.Data;
import lombok.Getter;
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
public class IdManager {


    @Getter
    private AtomicLong maxMerchantId = new AtomicLong();
    @Getter
    private AtomicLong maxUserId = new AtomicLong();
    @Autowired
    MongoTemplate mongoTemplate;

    @PostConstruct
    void init() {
        Query queryMerchant = new Query().with(Sort.by(Sort.Direction.DESC, "_id")).limit(1);
        MerchantEntity merchant = mongoTemplate.findOne(queryMerchant, MerchantEntity.class);
        if (merchant != null) {
            maxMerchantId.set(merchant.getId());
        }
        Query queryUser = new Query().with(Sort.by(Sort.Direction.DESC, "_id")).limit(1);
        User user = mongoTemplate.findOne(queryUser, User.class);
        if (user != null) {
            maxUserId.set(user.getId());
        }
    }
}
