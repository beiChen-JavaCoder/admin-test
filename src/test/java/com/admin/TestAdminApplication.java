package com.admin;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import com.admin.component.IdManager;
import com.admin.constants.SystemConstants;
import com.admin.dao.UserRepository;
import com.admin.domain.ResponseResult;
import com.admin.domain.dto.MerchantDto;
import com.admin.domain.entity.Menu;
import com.admin.domain.entity.MerchantBean;
import com.admin.domain.entity.User;
import com.admin.domain.entity.UserRole;
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
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

    @Test
    void insertTest1() {
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
    void selectPermsByUserId() {
        Query query = new Query();
        query.addCriteria(Criteria.where("status").is(Integer.valueOf(SystemConstants.STATUS_NORMAL)));
        query.addCriteria(Criteria.where("menu_type").in(SystemConstants.MENU, SystemConstants.BUTTON));
        List<Menu> menus = mongoTemplate.find(query, Menu.class);
        log.info(menus.toString());
    }

    @Test
    void InsertMerchant() {
        for (Integer i = 0; i < 10; i++) {
            MerchantBean merchantEntity = new MerchantBean();
            merchantEntity.setId(ThreadLocalRandom.current().nextLong());
//            merchantEntity.setName("商家"+i);
//            merchantEntity.setQq(11111+i);
//            merchantEntity.setWx("11111"+i);
//            merchantEntity.setYy("11111"+i);
//            merchantEntity.setRatio(1000+i);
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

        log.info(result + "1111111111111111111111111111111111111111111111111");
    }

    @Test
    void test2() {
        Query query = new Query();
        query.addCriteria(Criteria.where("status").is(SystemConstants.STATUS_NORMAL));
//        query.addCriteria(Criteria.where("menu_type").in(SystemConstants.MENU, SystemConstants.BUTTON));
        List<Menu> menus = mongoTemplate.find(query, Menu.class);
        List<String> perms = menus.stream()
                .map(Menu::getPerms)
                .collect(Collectors.toList());
        log.info(perms.toString() + "1111111111111111111111111111111111");
        log.info(menus.toString() + "1111111111111111111111111111111111");
    }

    @Test
    void findUserPage() {
        User user = mongoTemplate
                .findOne(Query.query(Criteria
                        .where("user_name")
                        .is("test1")), User.class);
        ResponseResult userPage = userService.findUserPage(user, 1, 5);
        log.info(userPage.getData().toString());


    }

    @Test
    void removeMerchantById() {
        ArrayList<Long> ids = new ArrayList<>();
        ids.add(7L);
        ids.add(8L);
        ids.add(9L);
        ids.add(10L);
//        ResponseResult<PageVo> pageVoResponseResult = merchantService.removeMerchantById(strings);
        Query query = new Query(Criteria.where("_id").in(ids));
        String remove = mongoTemplate.remove(query, MerchantBean.class).toString();
        log.info(remove);

    }

    @Test
    public void insertDocument() {
        User user = new User();
        user.setId(AtomicIdGenerator.generateId());
        user.setType("1");
        user.setCreateTime(DateUtil.date());
        user.setUserName("test1");
        String test1 = bCryptPasswordEncoder.encode("test1");
        user.setPassword(test1);
        user.setStatus("0");
        user.setNickName("test1");
        user.setDelFlag(0);
        user.setUpdateTime(DateUtil.date());
        mongoTemplate.save(user);
    }
private IdManager idManager;
    @Test
    @Transactional
    void testTransactional() {

        User user = new User();
        user.setId(100L);
        mongoTemplate.insert(user);
        try {
            log.info("用户添加完成，等待执行。。。");
            // 暂停当前线程执行500毫秒（0.5秒）
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            // 处理线程中断异常
            e.printStackTrace();
        }
        mongoTemplate.insert(new UserRole());
    }
    @Test
    void getScore(){

//        JSONArray jsonArray = Notification.getGameNotification().getJSONArray();
        String controlScoreNotification = HttpUtil.post("http://192.168.10.62:9998/control/getControlConfigs","");
        JSONArray objects = JSONArray.parseArray(controlScoreNotification);
        ArrayList<JSONObject> jsonObjects = new ArrayList<>();
        for (Object reGame : objects) {
            jsonObjects.add((JSONObject) reGame);
        }
    }
    @Test
    void zhengzebiaodashi(){

        String regex = "^(?!0$)([1-9]\\d{0,3}|10000)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher("1000");
        if (!matcher.matches()) {
            log.error("匹配失败");
        }else {
            log.info("匹配成功");
        }
    }

}
