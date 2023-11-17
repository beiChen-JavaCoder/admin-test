package com.admin.service.Imp;

import com.admin.constants.SystemConstants;
import com.admin.domain.entity.Role;
import com.admin.domain.entity.UserRole;
import com.admin.notification.Notification;
import com.admin.service.RoleInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

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
    private Notification notification;



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
