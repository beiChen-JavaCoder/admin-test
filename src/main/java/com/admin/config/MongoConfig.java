//package com.admin.config;
//
//import com.admin.component.GameMongoTemplate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import com.mongodb.client.MongoClient;
//
//@Configuration
//public class MongoConfig {
//
//    @Autowired
//    @Bean
//    public GameMongoTemplate gameMongoTemplate(MongoClient mongoClient) {
//        return new GameMongoTemplate(mongoClient, "game");
//    }
//}
