package com.admin.component;

import com.admin.config.MongoUtil;
import com.admin.domain.entity.Menu;
import com.admin.domain.entity.MerchantEntity;
import com.admin.domain.entity.Role;
import com.admin.domain.entity.User;
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
    @Getter
    private AtomicLong maxMenuId = new AtomicLong();
    @Getter
    private AtomicLong maxRoleId = new AtomicLong();

    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    MongoUtil mongoUtil;

    @PostConstruct
    void init() {
        MongoTemplate gameTemplate = mongoUtil.getGameTemplate();
        Query queryMerchant = new Query().with(Sort.by(Sort.Direction.DESC, "_id")).limit(1);
        MerchantEntity merchant = gameTemplate.findOne(queryMerchant, MerchantEntity.class);
        if (merchant != null) {
            maxMerchantId.set(merchant.getId());
        }
        Query queryUser = new Query().with(Sort.by(Sort.Direction.DESC, "_id")).limit(1);
        User user = mongoTemplate.findOne(queryUser, User.class);
        if (user != null) {
            maxUserId.set(user.getId());
        }
        Query queryMenu = new Query().with(Sort.by(Sort.Direction.DESC, "_id")).limit(1);
        Menu menu = mongoTemplate.findOne(queryMenu, Menu.class);
        if (menu != null) {
            maxMenuId.set(menu.getId());
        }
        Query queryRole = new Query().with(Sort.by(Sort.Direction.DESC, "_id")).limit(1);
        Role role = mongoTemplate.findOne(queryRole, Role.class);
        if (role != null) {
            maxRoleId.set(role.getId());
        }
    }
}
