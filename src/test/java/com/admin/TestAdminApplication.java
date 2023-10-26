package com.admin;

import cn.hutool.core.date.DateUtil;
import com.admin.constants.SystemConstants;
import com.admin.dao.UserRepository;
import com.admin.domain.entity.Menu;
import com.admin.domain.entity.MerchantBean;
import com.admin.domain.dto.MerchantDto;
import com.admin.domain.entity.User;
import com.admin.service.MerchantService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@SpringBootTest(classes = AdminTest.class)
@Slf4j
public class TestAdminApplication {

    @Autowired
    UserRepository userRepository;
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    MerchantService merchantService;

    @Test
    void insertTest1(){
        User user = new User();
        user.setUserName("test1");
        user.setNickName("test1");
        user.setType("1");
        user.setStatus("0");
        user.setDelFlag(1);
        user.setUpdateBy(null);
        user.setCreateBy(null);
        user.setRoleIds(null);
        Date date = DateUtil.date();
        user.setCreateTime(date);
        user.setUpdateTime(date);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = encoder.encode("test1");
        user.setPassword(password);
        System.out.println(user);
        userRepository.insert(user);
    }
    @Test
    void selectPermsByUserId(){
        Query query = new Query();
        query.addCriteria(Criteria.where("status").is(Integer.valueOf(SystemConstants.STATUS_NORMAL)));
        query.addCriteria(Criteria.where("menu_type").in(SystemConstants.MENU, SystemConstants.BUTTON));
        List<Menu> menus = mongoTemplate.find(query, Menu.class);
        log.info(menus.toString());
    }
    @Test
    void InsertMerchant(){
        for (Integer i = 0; i<10; i++) {
            MerchantBean merchantEntity = new MerchantBean();
            merchantEntity.setId(ThreadLocalRandom.current().nextLong());
            merchantEntity.setName("商家"+i);
            merchantEntity.setQq(11111+i);
            merchantEntity.setWx("11111"+i);
            merchantEntity.setYy("11111"+i);
            merchantEntity.setRatio(1000+i);
            mongoTemplate.insert(merchantEntity);
        }


    }
    @Test
    void test1() throws JsonProcessingException {


        ObjectMapper objectMapper = new ObjectMapper();

        // 创建 MerchantBean 对象
        MerchantDto merchantBean = new MerchantDto();
        merchantBean.setType(1);

        // 将 MerchantBean 对象转换为 JSON 字符串
        String json = objectMapper.writeValueAsString(merchantBean);

        WebClient webClient = WebClient.create("http://192.168.10.62:9998");
        Mono<String> result = webClient.post()
                .uri("/hall/merchant/mercantListChange")
                .contentType(MediaType.APPLICATION_JSON)  // 设置请求体的内容类型
                .body(BodyInserters.fromValue(json))  // 设置请求体的内容
                .retrieve()
                .bodyToMono(String.class);

        log.info(result+"1111111111111111111111111111111111111111111111111");
    }
    @Test
    void test2(){
                    Query query = new Query();
        query.addCriteria(Criteria.where("status").is(SystemConstants.STATUS_NORMAL));
//        query.addCriteria(Criteria.where("menu_type").in(SystemConstants.MENU, SystemConstants.BUTTON));
            List<Menu> menus = mongoTemplate.find(query, Menu.class);
            List<String> perms = menus.stream()
                    .map(Menu::getPerms)
                    .collect(Collectors.toList());
            log.info(perms.toString()+"1111111111111111111111111111111111");
            log.info(menus.toString()+"1111111111111111111111111111111111");
    }



}
