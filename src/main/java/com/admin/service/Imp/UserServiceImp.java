package com.admin.service.Imp;

import com.admin.domain.ResponseResult;
import com.admin.domain.dto.MerchantDto;
import com.admin.domain.entity.MerchantBean;
import com.admin.domain.entity.User;
import com.admin.domain.entity.UserRole;
import com.admin.domain.vo.MerchantVo;
import com.admin.domain.vo.PageVo;
import com.admin.enums.MerchantTypeEnum;
import com.admin.enums.UserTypeEnum;
import com.admin.notification.Notification;
import com.admin.service.UserRoleService;
import com.admin.service.UserService;
import com.admin.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImp implements UserService {
    @Resource
    private MongoTemplate mongoTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRoleService userRoleService;

    public User findUserByUserName(String userName) {

        return mongoTemplate.findOne(Query
                .query(Criteria
                        .where("user_name")
                        .is(userName)), User.class);
    }

    @Override
    public ResponseResult findUserPage(User user, Integer pageNum, Integer pageSize) {
        // 创建查询条件
        List<Criteria> criteriaList = new ArrayList<>();
        if (StringUtils.hasText(user.getUserName())) {
            criteriaList.add(Criteria.where("userName").regex(user.getUserName()));
        }
        if (StringUtils.hasText(user.getStatus())) {
            criteriaList.add(Criteria.where("status").is(user.getStatus()));
        }

        Criteria criteria = new Criteria();
        if (!criteriaList.isEmpty()) {
            criteria.andOperator(criteriaList.toArray(new Criteria[0]));
        }

        // 创建分页请求和排序，默认按_id升序
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, Sort.by("id"));

        // 创建查询对象
        Query query = Query.query(criteria).with(pageable);

        // 使用 MongoTemplate 执行查询：
        List<User> users = mongoTemplate.find(query, User.class);

        // 统计总数
        long total = mongoTemplate.count(Query.of(query).limit(-1).skip(-1), User.class);

        //封装成分页结果
        Page<User> userPage = new PageImpl(users, pageable, total);

        PageVo pageVo = new PageVo();
        pageVo.setTotal(total);
        pageVo.setRows(users);
        return ResponseResult.okResult(pageVo);

    }

    @Override
    public boolean checkUserNameUnique(String userName) {

        Query query = new Query(Criteria.where("user_name").is(userName));
        return mongoTemplate.count(query, User.class)==0;
    }

    @Override
    public ResponseResult addUser(User user) {
        for (String roleId : user.getRoleIds()) {
            if (roleId.equals(UserTypeEnum.admin.getCode())) {
                user.setType(UserTypeEnum.admin.getCode());
                break;
            }
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreateTime(new Date());

        mongoTemplate.save(user);

        if(user.getRoleIds()!=null&&user.getRoleIds().length>0){
            insertUserRole(user);
        }

        return ResponseResult.okResult();
    }
    private void insertUserRole(User user) {
        List<UserRole> sysUserRoles = Arrays.stream(user.getRoleIds())
                .map(roleId -> new UserRole(user.getId(), roleId)).collect(Collectors.toList());
        mongoTemplate.insert(sysUserRoles,UserRole.class);
    }


//    public ResponseResult addMerchant(MerchantVo merchantVo) {
//
//        MerchantBean merchantBean = BeanCopyUtils.copyBean(merchantVo, MerchantBean.class);
//
//        if (mongoTemplate.insert(merchantBean)!=null) {
//            Notification.notificationMerchant(new MerchantDto(MerchantTypeEnum.LIST.getType()));
//            return ResponseResult.okResult();
//        }else {
//            return ResponseResult.errorResult(500,"新增商户失败");
//        }
//
//    }


}
