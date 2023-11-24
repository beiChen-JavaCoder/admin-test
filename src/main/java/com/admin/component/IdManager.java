package com.admin.component;

import com.admin.config.MongoUtil;
import com.admin.domain.entity.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Xqf
 * @version 1.0
 */
@Component
public class IdManager {
    /**
     * 商户原子id
     */
    @Getter
    private AtomicLong maxMerchantId = new AtomicLong();
    /**
     * 用户原子id
     */
    @Getter
    private AtomicLong maxUserId = new AtomicLong();
    /**
     * 菜单原子id
     */
    @Getter
    private AtomicLong maxMenuId = new AtomicLong();
    /**
     * 角色原子id
     */
    @Getter
    private AtomicLong maxRoleId = new AtomicLong();
    /**
     * 机器人原子id
     */
    @Getter
    private AtomicLong maxRobotId = new AtomicLong();
    /**
     * 登录日志原子id
     */
    @Getter
    private AtomicLong maxLogininforId = new AtomicLong();
    /**
     * 操作日志原子id
     */
    @Getter
    private AtomicLong maxoperId = new AtomicLong();


    /**
     * 操作角色菜单原子id
     */
    @Getter
    private AtomicLong maxRoleMenuId = new AtomicLong();

    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    MongoUtil mongoUtil;

    @PostConstruct
    void init() {
        MongoTemplate gameTemplate = mongoUtil.getGameTemplate();
        Query queryMerchant = new Query().with(Sort.by(Sort.Direction.DESC, "_id")).limit(1);
        MerchantEntity merchant = gameTemplate.findOne(queryMerchant, MerchantEntity.class);
        if (merchant != null) {
            maxMerchantId.set(merchant.getId());
        }
        Query queryUser = new Query().with(Sort.by(Sort.Direction.DESC, "_id")).limit(1);
        User user = mongoTemplate.findOne(queryUser, User.class);
        if (user != null) {
            maxUserId.set(user.getId());
        }
        Query queryMenu = new Query().with(Sort.by(Sort.Direction.DESC, "_id")).limit(1);
        Menu menu = mongoTemplate.findOne(queryMenu, Menu.class);
        if (menu != null) {
            maxMenuId.set(menu.getId());
        }
        Query queryRole = new Query().with(Sort.by(Sort.Direction.DESC, "_id")).limit(1);
        Role role = mongoTemplate.findOne(queryRole, Role.class);
        if (role != null) {
            maxRoleId.set(role.getId());
        }
        Query queryRobot = new Query().with(Sort.by(Sort.Direction.DESC, "_id")).limit(1);
        Robot robot = gameTemplate.findOne(queryRobot, Robot.class);
        if (robot != null) {
            maxRobotId.set(robot.getId());
        }
        Query queryLoingor = new Query().with(Sort.by(Sort.Direction.DESC, "_id")).limit(1);
        SysLogininfor loingor = mongoTemplate.findOne(queryLoingor, SysLogininfor.class);
        if (loingor != null) {
            maxLogininforId.set(loingor.getInfoId());
        }
        Query queryOper = new Query().with(Sort.by(Sort.Direction.DESC, "_id")).limit(1);
        SysOperLog oper = mongoTemplate.findOne(queryOper, SysOperLog.class);
        if (oper != null) {
            maxoperId.set(oper.getOperId());
        }
        Query queryRoleMenu = new Query().with(Sort.by(Sort.Direction.DESC, "_id")).limit(1);
        RoleMenu roleMenu = mongoTemplate.findOne(queryRoleMenu, RoleMenu.class);
        if (roleMenu != null) {
            maxRoleMenuId.set(roleMenu.getId());
        }
    }
}
