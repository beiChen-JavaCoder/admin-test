package com.admin;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import com.admin.annotation.Log;
import com.admin.component.IdManager;
import com.admin.constants.SystemConstants;
import com.admin.dao.UserRepository;
import com.admin.domain.ResponseResult;
import com.admin.domain.dto.MerchantDto;
import com.admin.domain.entity.Menu;
import com.admin.domain.entity.MerchantBean;
import com.admin.domain.entity.User;
import com.admin.domain.entity.UserRole;
import com.admin.enums.BusinessType;
import com.admin.enums.OperatorType;
import com.admin.notification.Notification;
import com.admin.service.MerchantService;
import com.admin.service.UserService;
import com.admin.utils.AtomicIdGenerator;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;

@SpringBootTest(classes = AdminTest.class)
@EnableTransactionManagement
@Slf4j
public class TestAdminApplication {

    @Autowired
    UserRepository userRepository;
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    MerchantService merchantService;
    @Autowired
    UserService userService;
    @Resource
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Log(title = "测试类",businessType = BusinessType.OTHER,operatorType = OperatorType.TEST)
    @Test
void testLog(){

        log.info("11111111111111111111");


    }}


