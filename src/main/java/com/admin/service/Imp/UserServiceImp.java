package com.admin.service.Imp;

import com.admin.domain.entity.User;
import com.admin.service.UserService;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImp implements UserService {
    @Resource
    private MongoTemplate mongoTemplate;

    public User findUserByUserName(String userName){

         return mongoTemplate.findOne(Query
                .query(Criteria
                        .where("user_name")
                        .is(userName)), User.class);
    }

}
