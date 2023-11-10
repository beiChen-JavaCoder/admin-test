package com.admin.service.Imp;

import com.admin.domain.ResponseResult;
import com.admin.service.RobotService;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Xqf
 * @version 1.0
 */
@Service
@Slf4j
public class RobotServiceImp implements RobotService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public ResponseResult importRobot(List<JSONObject> robots) {
        ArrayList<String> robotList = new ArrayList<>();
        robots.forEach(robot -> {
            robotList.add(robot.toJSONString());
        });


        if (robotList.size() == 0) {
            log.error("上传的机器人不能为空");
            return ResponseResult.errorResult(500, "上传的机器人不能为空");
        }

//        Collection<String> strings = mongoTemplate.insertAll(robotList);
//        if (strings == null) {
//            return ResponseResult.errorResult(500,"插入机器人失败");
//        }
        log.info("插入机器人：" + robotList);
        return ResponseResult.okResult();


    }
}
