package com.admin.service.Imp;

import com.admin.constants.SystemConstants;
import com.admin.domain.ResponseResult;
import com.admin.domain.dto.MerchantDto;
import com.admin.domain.entity.Role;
import com.admin.domain.entity.GameRole;
import com.admin.domain.entity.UserRole;
import com.admin.domain.vo.PageVo;
import com.admin.domain.vo.RechargeVo;
import com.admin.domain.vo.RoleInfoVo;
import com.admin.enums.AppHttpCodeEnum;
import com.admin.enums.MerchantTypeEnum;
import com.admin.exception.SystemException;
import com.admin.service.RoleInfoService;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
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


    @Override
    public ResponseResult<PageVo> findRolePage(RoleInfoVo roleInfoVo, Integer pageNum, Integer pageSize) {

        // 创建查询条件
        List<Criteria> criteriaList = new ArrayList<>();
        if (StringUtils.hasText(roleInfoVo.getUserName())) {
            criteriaList.add(Criteria.where("user_name").regex(roleInfoVo.getUserName()));
        }
        Criteria criteria = new Criteria();
        if (!criteriaList.isEmpty()) {
            criteria.andOperator(criteriaList.toArray(new Criteria[0]));
        }

        //封装分页条件
        //创建分页请求和排序，默认按_id升序
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, Sort.by("_id"));
        // 创建查询对象
        Query query = Query.query(criteria).with(pageable);

        List<GameRole> roleInfoEntities = mongoTemplate.find(query, GameRole.class);

        // 统计总数
        long total = mongoTemplate.count(Query.of(query).limit(-1).skip(-1), GameRole.class);
        //封装返回结果
        PageVo pageVo = new PageVo();
        pageVo.setTotal(total);
        pageVo.setRows(roleInfoEntities);
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult updateRoleGold(RechargeVo rechargeVo) {

        Query query = Query.query(Criteria.where("_id").is(rechargeVo.getRid()));


        GameRole roleInfo = mongoTemplate.findOne(query, GameRole.class);

        try {
            //当充值对象不存在时报错
            Assert.isTrue(roleInfo != null);
        } catch (Exception e) {
            throw new SystemException(AppHttpCodeEnum.RECHARGE_NO);
        }
        Update update = new Update().inc("gold", rechargeVo.getNum());
        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, GameRole.class);
        if (updateResult.wasAcknowledged() && updateResult.getModifiedCount() == 1L) {
            //更新金币成功后组装通知参数
            MerchantDto merchantDto = new MerchantDto();
            merchantDto.setMerchantId(rechargeVo.getMerchantId());
            merchantDto.setType(MerchantTypeEnum.CHARGE.getType());
            merchantDto.setUserId(roleInfo.getRid());
            merchantDto.setChangeNum(rechargeVo.getNum());

            return ResponseResult.okResult(200, "充值成功");

        } else {

            return ResponseResult.errorResult(500, "充值失败");

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
