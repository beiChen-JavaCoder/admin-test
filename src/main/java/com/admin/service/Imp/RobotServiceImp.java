package com.admin.service.Imp;

import com.admin.component.IdManager;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
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
    @Autowired
    private IdManager idManager;


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
        //给文件去重
        List<Robot> deduplicatedList = robots.stream().distinct().collect(Collectors.toList());

        MongoTemplate gameTemplate = mongoUtil.getGameTemplate();
        List<Robot> robotAll = gameTemplate.findAll(Robot.class);
        //过滤所有的名字相同的机器人数据
        List<Robot> robotList = deduplicatedList.stream().filter(
                robot -> robotAll.stream().noneMatch((
                        robota -> robot.getRobotName()
                                .equals(robota.getRobotName())))).collect(Collectors.toList());
        //给id赋值
        robotList.forEach(robot -> {
            robot.setId(idManager.getMaxRobotId().incrementAndGet());
        });

        Collection<Robot> rerobots = gameTemplate.insertAll(robotList);

        if (rerobots.isEmpty()) {
            throw new SystemException(AppHttpCodeEnum.UPLOAL_ROBOT_NAME);
        }

        log.info("变更机器人名称发起通知");
        String remsg = notification.updateRobotName();
        log.info("变更机器人名称获取回调：" + remsg);

        if (remsg.equals("1")) {
            log.info("插入机器人失败原因：请求：" + remsg + "插入数据：" + rerobots);
            return ResponseResult.errorResult(500, "插入机器人失败或通知失败");
        }
        log.info("插入机器人结果：" + rerobots);

        return ResponseResult.okResult();


    }

    @Override
    public ResponseResult robotControlList() {
        Long userId = SecurityUtils.getUserId();
        List<JSONObject> robotList = notification.getRobotList();
        robotList.forEach(robot -> {
            JSONObject betTime = (JSONObject) robot.get("betTime");
            //换算为秒
            Integer min = Integer.parseInt(String.valueOf(betTime.get("min"))) / 1000;
            Integer max = Integer.parseInt(String.valueOf(betTime.get("max"))) / 1000;
            betTime.put("max", max);
            betTime.put("min", min);
            robot.put("betTime", betTime);
        });
        if (robotList.size() == 0) {
            return ResponseResult.errorResult(500, "请求失败");
        }
        PageVo pageVo = new PageVo();
        pageVo.setRows(robotList);
        pageVo.setTotal(Long.valueOf(robotList.size()));

        log.info(" 用户id：" + userId + "获取了机器人信息列表:" + pageVo);

        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult updateRobot(RobotBeanVo robot) {


        RobotBean robotBean = new RobotBean();
        if (robot.getMax() != null && robot.getMin() != null) {
            robotBean = BeanCopyUtils.copyBean(robot, RobotBean.class);
        }
        if (RobotBean.Type.initScore.getNum() == (robot.getType())) {
            //校验参数
            if (!(robot.getMax() <= 9999999999L && robot.getMin() > 0)) {
                return ResponseResult.errorResult(500, "请输入合规的参数 最小>0,最大<=99亿");
            }
            robotBean.setType(RobotBean.Type.initScore.getNum());

        } else if (RobotBean.Type.betScore.getNum() == (robot.getType())) {
            //校验参数
            if (!(robot.getMax() <= 9999999999L && robot.getMin() > 0)) {
                return ResponseResult.errorResult(500, "请输入合规的参数 最小>0,最大<=99亿");
            }
            robotBean.setType(RobotBean.Type.betScore.getNum());

        } else if (RobotBean.Type.carryScore.getNum() == (robot.getType())) {
            //校验参数
            if (!(robot.getMax() <= 9999999999L && robot.getMin() > 0)) {
                return ResponseResult.errorResult(500, "请输入合规的参数 最小>0,最大<=99亿");
            }
            robotBean.setType(RobotBean.Type.carryScore.getNum());

        } else if (RobotBean.Type.betTime.getNum() == (robot.getType())) {
            //校验参数
            if (!(robot.getMax() <= 30 && robot.getMin() > 0)) {
                return ResponseResult.errorResult(500, "请输入合规的参数 最小>1,最大<=30");
            }

            robotBean.setMax((robot.getMax()) * 1000);
            robotBean.setMin((robot.getMin()) * 1000);
            robotBean.setType(RobotBean.Type.betTime.getNum());
        } else if (RobotBean.Type.betRatio.getNum() == (robot.getType())) {
            //校验参数
            if (!(robot.getBetRatio() <= 10000 && robot.getBetRatio() > 0)) {
                return ResponseResult.errorResult(500, "请输入合规的参数 最小>1,最大<=10000");
            }
            robotBean.setType(RobotBean.Type.betRatio.getNum());
            robotBean.setBetRatio(robot.getBetRatio());
            robotBean.setGameId(robot.getGameId());
        }

        JSONObject json = (JSONObject) JSONObject.toJSON(robotBean);
        Long userId = SecurityUtils.getUserId();
        String remsg = notification.updateRobotControl(json);
        if (remsg.equals("0")) {
            log.info("用户：" + userId + "修改机器人控制成功,修改对象：" + robotBean);
            return ResponseResult.okResult(200, "修改机器人控制成功");
        }
        log.error("用户：" + userId + "修改机器人控制失败,失败信息：" + remsg);
        return null;
    }

    @Override
    public ResponseResult findRobotPage(Integer pageNum, Integer pageSize) {
        Long userId = SecurityUtils.getUserId();

        log.info("用户id：" + userId + "访问了机器人名称列表");

        ArrayList<Criteria> criteriaList = new ArrayList<>();

        Criteria criteria = new Criteria();
        if (!criteriaList.isEmpty()) {
            criteria.andOperator(criteriaList.toArray(new Criteria[0]));
        }
        // 创建分页请求和排序，默认按创建时间降序
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);

        // 创建查询对象
        Query query = Query.query(criteria).with(pageable);

        MongoTemplate gameTemplate = mongoUtil.getGameTemplate();

        //查询机器人
        List<Robot> robotNames = gameTemplate.find(query, Robot.class);
        //统计总数
        long total = gameTemplate.count(Query.of(query).limit(-1).skip(-1), Robot.class);

        PageVo pageVo = new PageVo();
        pageVo.setRows(robotNames);
        pageVo.setTotal(Long.valueOf(total));
        log.info("用户id：" + userId + "获取机器人名称列表：" + pageVo);
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult updateRobotName(Robot robot) {
        Long userId = SecurityUtils.getUserId();
        log.info("用户id:"+userId+"正在删除"+robot.getId()+"号机器人");
        MongoTemplate gameTemplate = mongoUtil.getGameTemplate();

        Update update = new Update();
        update.set("robot_name", robot.getRobotName());
        boolean re = gameTemplate.updateFirst(
                        Query.query(Criteria.where("_id").is(robot.getId())),
                        update, Robot.class)
                .wasAcknowledged();
        if (re) {
            return ResponseResult.okResult(200, "更新成功");
        }

        return ResponseResult.errorResult(500, "更新失败");
    }

    @Override
    public ResponseResult delRobot(Long robotId) {
        Long userId = SecurityUtils.getUserId();
        log.info("用户id:"+userId+"正在删除"+robotId+"号机器人");
        MongoTemplate gameTemplate = mongoUtil.getGameTemplate();
        boolean result = gameTemplate
                .remove(Query.query(Criteria.where("_id")
                        .is(robotId)), Robot.class)
                .wasAcknowledged();
        if (result) {
            log.info("用户id:"+userId+"成功删除"+robotId+"号机器人");
            return ResponseResult.okResult(200, "删除成功");
        }
        log.info("用户id:"+userId+"删除失败"+robotId+"号机器人");
        return ResponseResult.errorResult(500, "删除失败");

    }

}
