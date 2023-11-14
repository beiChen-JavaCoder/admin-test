package com.admin.service.Imp;

import com.admin.constants.SystemConstants;
import com.admin.domain.ResponseResult;
import com.admin.domain.dto.MerchantDto;
import com.admin.domain.entity.Role;
import com.admin.domain.entity.GameRole;
import com.admin.domain.entity.User;
import com.admin.domain.entity.UserRole;
import com.admin.domain.vo.PageVo;
import com.admin.domain.vo.RechargeVo;
import com.admin.domain.vo.RoleInfoVo;
import com.admin.enums.AppHttpCodeEnum;
import com.admin.enums.MerchantTypeEnum;
import com.admin.exception.SystemException;
import com.admin.notification.Notification;
import com.admin.service.RoleInfoService;
import com.admin.utils.SecurityUtils;
import com.mongodb.client.*;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.management.relation.RoleInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Xqf
 * @version 1.0
 */
@Slf4j
@Service
public class RoleInfoServiceImp implements RoleInfoService {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private MongoClient mongoClient;
    private MongoTemplate gameTemplate;
    @Autowired
    private Notification notification;


    private  MongoTemplate getGameTemplate() {
        if (gameTemplate == null) {
            gameTemplate = new MongoTemplate(mongoClient, "game");
        }
        return gameTemplate;
    }

    @Override
    public ResponseResult<PageVo> findRolePage(RoleInfoVo roleInfoVo, Integer pageNum, Integer pageSize) {
        // 创建查询条件
        List<Criteria> criteriaList = new ArrayList<>();
        if (!(roleInfoVo.getId() ==null)) {
            criteriaList.add(Criteria.where("_id").is(roleInfoVo.getId()));
        }
        Criteria criteria = new Criteria();
        if (!criteriaList.isEmpty()) {
            criteria.andOperator(criteriaList.toArray(new Criteria[0]));
        }

        // 封装分页条件
        // 创建分页请求和排序，默认按_id升序
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, Sort.by("_id"));
        // 创建查询对象
        Query query = Query.query(criteria).with(pageable);
        // 执行查询操作
        List<GameRole> roleInfos = getGameTemplate().find(query, GameRole.class);
        // 统计总数
        long total = roleInfos.size();

        // 封装返回结果
        PageVo pageVo = new PageVo();
        pageVo.setTotal(total);
        pageVo.setRows(roleInfos);

        log.info("查询玩家待充值列表：" + roleInfos);
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult updateRoleGold(RechargeVo rechargeVo) {

        //获取登录用户查询绑定的商户
        Long userId = SecurityUtils.getUserId();
        MerchantDto merchantDto = new MerchantDto();
        Long merchantEntId = mongoTemplate.findById(userId, User.class).getMerchantEntId();
        merchantDto.setMerchantId(merchantEntId);
        merchantDto.setType(MerchantTypeEnum.CHARGE.getType());
        merchantDto.setChangeNum(rechargeVo.getNum());
        merchantDto.setUserId(rechargeVo.getRid());
        String reRecharge = notification.notificationMerchant(merchantDto);
        try {
            if (String.valueOf(AppHttpCodeEnum.NOTIFICATION_SUCCESS.getCode()).equals(reRecharge)) {
                return ResponseResult.okResult(200, "充值成功");
            } else {
                return ResponseResult.errorResult(500, "充值失败");
            }
        } finally {
            log.info("商户" + merchantEntId + "对玩家" + merchantDto.getUserId() + "充值" + merchantDto.getChangeNum());
        }
    }

    @Override
    public List<Role> findRoleAll() {

        Query query = Query.query(Criteria.where("status").is(SystemConstants.STATUS_NORMAL));

        return mongoTemplate.find(query, Role.class);
    }

    @Override
    public List<Long> findRoleIdByUserId(Long userId) {

        Query query = new Query(Criteria.where("user_id").is(userId));
        List<UserRole> userRoles = mongoTemplate.find(query, UserRole.class, "sys_user_role");
        List<Long> roleIds = userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toList());

        return roleIds;
    }
}
