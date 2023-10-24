package com.admin;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.admin.AdminTest;
import com.admin.dao.UserRepository;
import com.admin.domain.entity.User;
import javafx.scene.chart.PieChart;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Date;
import java.util.UUID;

@SpringBootTest(classes = AdminTest.class)
public class TestAdminApplication {

    @Autowired
    UserRepository userRepository;

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


}
