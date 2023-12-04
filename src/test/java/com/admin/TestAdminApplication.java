//package com.admin;
//
//import com.admin.annotation.Log;
//import com.admin.dao.UserRepository;
//import com.admin.enums.BusinessType;
//import com.admin.enums.OperatorType;
//import com.admin.service.MerchantService;
//import com.admin.service.UserService;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//import javax.annotation.Resource;
//
//@SpringBootTest(classes = AdminTest.class)
//@EnableTransactionManagement
//@Slf4j
//public class TestAdminApplication {
//
//    @Autowired
//    UserRepository userRepository;
//    @Autowired
//    MongoTemplate mongoTemplate;
//    @Autowired
//    MerchantService merchantService;
//    @Autowired
//    UserService userService;
//    @Resource
//    BCryptPasswordEncoder bCryptPasswordEncoder;
//
//    @Log(title = "测试类", businessType = BusinessType.OTHER, operatorType = OperatorType.TEST)
//    @Test
//    public void testLog() {
//
//        log.info("11111111111111111111");
//
//
//    }
//}
//
//
