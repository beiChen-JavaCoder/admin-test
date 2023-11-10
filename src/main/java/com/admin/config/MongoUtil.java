package com.admin.config;

import com.mongodb.client.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

/**
 * @author Xqf
 * @version 1.0
 */
@Component
public class MongoUtil {

    private MongoTemplate gameTemplate;

    private MongoTemplate merchantOrderTemplate;

    @Autowired
    private MongoClient mongoClient;

    public MongoTemplate getGameTemplate() {
        if (gameTemplate == null) {
            gameTemplate = new MongoTemplate(mongoClient, "game");
        }
        return gameTemplate;
    }
    public  MongoTemplate getMerchantOrderTemplate() {
        if (merchantOrderTemplate == null) {
            merchantOrderTemplate = new MongoTemplate(mongoClient, "game");
        }
        return merchantOrderTemplate;
    }
}
