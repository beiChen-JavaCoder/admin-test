package com.admin;

import com.admin.AdminTest;
import com.admin.dao.UserRepository;
import com.admin.domain.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = AdminTest.class)
public class TestAdminApplication {

    @Autowired
    UserRepository userRepository;

    @Test
    void insertTest1(){
        User user = new User();
        user.setUserName("test1");
        user.setNickName("test1");
        user.setPassword("test1");
        userRepository.insert(user);
    }


}
