package com.admin.service.Imp;

import com.admin.config.MongoUtil;
import com.admin.domain.ResponseResult;
import com.admin.domain.entity.Robot;
import com.admin.domain.entity.RobotBean;
import com.admin.domain.vo.PageVo;
import com.admin.domain.vo.RobotBeanVo;
import com.admin.enums.AppHttpCodeEnum;
import com.admin.exception.SystemException;
import com.admin.notification.Notification;
import com.admin.service.RobotService;
import com.admin.utils.BeanCopyUtils;
import com.admin.utils.SecurityUtils;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.client.model.UpdateOptions;
import com.sun.org.apache.bcel.internal.generic.NEW;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Xqf
 * @version 1.0
 */
@Service
@Slf4j
public class RobotServiceImp implements RobotService {

    @Autowired
    private MongoUtil mongoUtil;
    @Autowired
    private Notification notification;


    @Override
    public ResponseResult importRobot(MultipartFile multipartFile) {
        Long userId = SecurityUtils.getUserId();
        log.info("用户id:" + userId + "正在变更机器人列表");
        byte[] bytes;
        try {
            bytes = multipartFile.getBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String string = new String(bytes);
        String[] splits = string.split(",");
        ArrayList<Robot> robots = new ArrayList<>();
        for (String split : splits) {
            Robot robot = new Robot();
            robot.setRobotName(split);
            robots.add(robot);
        }

        if (robots.size() == 0) {
            log.error("上传的机器人个数不能为空");
            return ResponseResult.errorResult(500, "上传的机器人不能为空");
        }

        HashSet<Robot> robotHashSet = new HashSet<>();

        List<Robot> robotAll = mongoUtil.getGameTemplate().findAll(Robot.class);
        //过滤所有的名字相同的机器人数据
        List<Robot> robotList = robots.stream().filter(
                robot -> robotAll.stream().noneMatch((
                        robota -> robot.getRobotName()
                                .equals(robota.getRobotName())))).collect(Collectors.toList());
        Collection<Robot> rerobots = mongoUtil.getGameTemplate().insertAll(robotList);

        if (rerobots.isEmpty()) {
            throw new SystemException(AppHttpCodeEnum.UPLOAL_ROBOT_NAME);
        }


        //关闭链接
        mongoUtil.closeGameClient();
        String remsg = notification.updateRobotName();

        if (remsg.equals("1")) {
            log.info("插入机器人失败原因：请求：" + remsg + "插入数据：" + rerobots);
            return ResponseResult.errorResult(500, "插入机器人失败或通知失败");
        }
        log.info("插入机器人结果：" + rerobots);

        return ResponseResult.okResult();


    }

    @Override
    public ResponseResult robotList() {
        Long userId = SecurityUtils.getUserId();
        List<JSONObject> robotList = notification.getRobotList();
        if (robotList.size() == 0) {
            return ResponseResult.errorResult(500, "请求失败");
        }
        PageVo pageVo = new PageVo();
        pageVo.setRows(robotList);
        pageVo.setTotal(Long.valueOf(robotList.size()));

        log.info(" 用户id："+userId+"获取了机器人信息列表:"+pageVo);

        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult updateRobot(RobotBeanVo robot) {


        RobotBean robotBean = new RobotBean();
        if (robot.getMax() != null) {
            robotBean = BeanCopyUtils.copyBean(robot, RobotBean.class);
        } else {
            robotBean.setType(RobotBean.Type.betRatio.getNum());
            robotBean.setBetRatio(robot.getBetRatio());
        }

        if (RobotBean.Type.initScore.getNum() == (robot.getType())) {
            robotBean.setType(RobotBean.Type.initScore.getNum());
        } else if (RobotBean.Type.betScore.getNum() == (robot.getType())) {
            robotBean.setType(RobotBean.Type.betScore.getNum());

        } else if (RobotBean.Type.carryScore.getNum() == (robot.getType())) {
            robotBean.setType(RobotBean.Type.carryScore.getNum());

        } else if (RobotBean.Type.betTime.getNum() == (robot.getType())) {
            robotBean.setType(RobotBean.Type.betTime.getNum());
        } else {
            robotBean.setGameId(robot.getGameId());
        }

        JSONObject json = (JSONObject) JSONObject.toJSON(robotBean);
        Long userId = SecurityUtils.getUserId();
        String remsg = notification.updateRobotControl(json);
        if (remsg.equals("0")) {
            log.info("用户：" + userId + "修改机器人控制成功,修改对象：" + robotBean);
            return ResponseResult.okResult();
        }
        log.error("用户：" + userId + "修改机器人控制失败,失败信息：" + remsg);
        return null;
    }

}
