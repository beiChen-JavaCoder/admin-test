package com.admin.service.Imp;

import cn.hutool.core.date.DateUtil;
import com.admin.component.IdManager;
import com.admin.domain.ResponseResult;
import com.admin.domain.dto.MerchantDto;
import com.admin.domain.entity.MerchantBean;
import com.admin.domain.entity.MerchantEntity;
import com.admin.domain.entity.User;
import com.admin.domain.entity.UserRole;
import com.admin.domain.vo.MerchantVo;
import com.admin.domain.vo.PageVo;
import com.admin.domain.vo.UserAndMerchantVo;
import com.admin.enums.AppHttpCodeEnum;
import com.admin.enums.MerchantTypeEnum;
import com.admin.enums.UserTypeEnum;
import com.admin.exception.SystemException;
import com.admin.notification.Notification;
import com.admin.service.MerchantService;
import com.admin.service.UserRoleService;
import com.admin.service.UserService;
import com.admin.utils.BeanCopyUtils;
import com.admin.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

@Service
@Slf4j
public class UserServiceImp implements UserService {
    @Resource
    private MongoTemplate mongoTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private IdManager idManager;

    @Autowired
    private MerchantService merchantService;

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
    public boolean checkUserNameUnique(String userName){

        Query query = new Query(Criteria.where("user_name").is(userName));
        return mongoTemplate.count(query, User.class) == 0;
    }

    @Override
    public ResponseResult addUser(UserAndMerchantVo userAndMerchantVo) {

        User user = BeanCopyUtils.copyBean(userAndMerchantVo, User.class);
        MerchantEntity merchantEntity = BeanCopyUtils.copyBean(userAndMerchantVo, MerchantEntity.class);

        merchantEntity.setId(idManager.getMaxMerchantId().incrementAndGet());
        try {
            //新增商户
            merchantService.addMerchant(merchantEntity);
        } catch (Exception e) {
            throw new SystemException(AppHttpCodeEnum.ADD_USER_MERCHANT_NO);
        }
        //判断新增用户类型
        for (Long type: user.getRoleIds()) {
            if ((type+"").equals(UserTypeEnum.admin.getCode())){
                user.setType(type+"");
                user.setType(UserTypeEnum.admin.getCode()+"");
                break;
            }else if ((type+"").equals(UserTypeEnum.common.getCode())){
                user.setType(type+"");
                user.setType(UserTypeEnum.common.getCode()+"");
                //给用户绑定商户id
                user.setMerchantEntId(merchantEntity.getId());
            }

        }
        //创建人（更新人）
        Long loginUserId = SecurityUtils.getUserId();
        user.setCreateBy(loginUserId);
        user.setUpdateBy(loginUserId);
        //密码加密处理
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        //新增创建(更新)时间
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        user.setDelFlag(0);
        //自增id
        user.setId(idManager.getMaxUserId().incrementAndGet());

        try {
            //新增用户
            mongoTemplate.save(user);
            //添加角色关联
            if (user.getRoleIds() != null && user.getRoleIds().length > 0) {
                insertUserRole(user);
            }

        } catch (Exception e) {
            throw new SystemException(AppHttpCodeEnum.ADD_USER_MERCHANT_NO);
        }

        user.setMerchantEntId(merchantEntity.getId());


        return ResponseResult.okResult();
    }

    @Override
    public User findUserById(Long userId) {
        Query query = Query.query(Criteria.where("id").is(userId));

        return mongoTemplate.findOne(query, User.class);
    }

    @Override
    public void removeByIds(List<Long> userIds) {

        Query query = Query.query(Criteria.where("_id").in(userIds));
        //删除对应用户
        mongoTemplate.remove(query, User.class);

        //删除对应的用户角色
        delUserRole(userIds);
    }

    @Override
    public void updateUser(User user) {
        // 删除用户与角色关联
        ArrayList<Long> userIds = new ArrayList<>();
        userIds.add(user.getId());
        delUserRole(userIds);
        //新增用户与角色关联
        insertUserRole(user);

        Query updateQuery = new Query(Criteria.where("_id").is(user.getId()));
        Update update = new Update();
        // 如果nick_name不为空，则添加更新操作
        if (user.getNickName() != null) {
            update.set("nick_name", user.getNickName());
        }
        if (user.getPassword() != null) {
            // 如果password不为空，则添加加密更新操作
            update.set("password",passwordEncoder.encode(user.getPassword()));
        }
        update.set("updateTime", new Date());
        mongoTemplate.updateFirst(updateQuery, update, User.class);
    }


    private void insertUserRole(User user) {
        List<UserRole> sysUserRoles = Arrays.stream(user.getRoleIds())
                .map(roleId -> new UserRole(user.getId(), roleId)).collect(Collectors.toList());
        mongoTemplate.insert(sysUserRoles, UserRole.class);
    }

    private void delUserRole(List<Long> userIds) {
        Query query = Query.query(Criteria.where("user_id").in(userIds));
        mongoTemplate.remove(query, UserRole.class);
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
