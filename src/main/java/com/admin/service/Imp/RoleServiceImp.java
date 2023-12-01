package com.admin.service.Imp;

import cn.hutool.core.date.DateUtil;
import com.admin.component.IdManager;
import com.admin.constants.SystemConstants;
import com.admin.domain.ResponseResult;
import com.admin.domain.entity.*;
import com.admin.domain.vo.PageVo;
import com.admin.service.MenuService;
import com.admin.service.RoleMenuService;
import com.admin.service.RoleService;
import com.admin.utils.BeanCopyUtils;
import com.admin.utils.RedisCache;
import com.admin.utils.SecurityUtils;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2022-08-09 22:36:47
 */
@Service
@Slf4j
public class RoleServiceImp implements RoleService {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private RoleMenuService roleMenuService;
    @Autowired
    private IdManager idManager;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private MenuService menuService;
    @Autowired
    private RoleService roleService;

    @Override
    public List<String> selectRoleKeyByUserId(Long id) {
        //判断是否是管理员 如果是返回集合中只需要有admin
        //id==1L
        if (id == 1L) {
            List<String> roleKeys = new ArrayList<>();
            roleKeys.add("admin");
            return roleKeys;
        }
        //否则返回该角色对应的权限
        //待测试
        return findRoleKeyByUserId(id);


    }

    @Override
    public List<Role> findRoleAll() {
        Query query = Query.query(Criteria.where("status").is(SystemConstants.STATUS_NORMAL));
        return mongoTemplate.find(query, Role.class);
    }

    @Override
    public ResponseResult findRolePage(Role role, Integer pageNum, Integer pageSize) {

        Query query = new Query();

        if (StringUtils.hasText(role.getRoleName())) {
            //添加模糊查询条件
            query.addCriteria(Criteria.where("roleName").regex(role.getRoleName()));
        }

        if (StringUtils.hasText(role.getStatus())) {
            //添加等于查询条件
            query.addCriteria(Criteria.where("status").is(role.getStatus()));
        }
        //添加排序查询条件以roleSort升序查询
        query.with(Sort.by(Sort.Order.asc("roleSort")));
        //执行分页查询
        long total = mongoTemplate.count(query, Role.class);

        query.skip((pageNum - 1) * pageSize).limit(pageSize);

        List<Role> roles = mongoTemplate.find(query, Role.class);
        //封装PageVo对象
        PageVo pageVo = new PageVo();
        pageVo.setTotal(total);
        pageVo.setRows(roles);

        return ResponseResult.okResult(pageVo);

    }

    @Override
    public Role findRoleById(Long roleId) {
        return mongoTemplate.findById(roleId, Role.class);
    }

    @Override
    public void addRole(Role role) {
        role.setId(idManager.getMaxRoleId().incrementAndGet());
        role.setCreateBy(SecurityUtils.getUserId());
        role.setCreateTime(DateUtil.date());
        role.setDelFlag(0 + "");

        mongoTemplate.insert(role);
        if (role.getMenuIds() != null && role.getMenuIds().length > 0) {
            addRoleMenu(role);
        }

    }

    @Override
    public void removeById(List<Long> ids) {
        //删除角色信息
        mongoTemplate.remove(Query.query(Criteria.where("_id").in(ids)), Role.class);
        //删除角色绑定的菜单信息
        roleMenuService.removeRoleMenuByRoleIds(ids);
    }

    @Override
    public void updateRole(Role role) {


        role.setUpdateTime(DateUtil.date());
        role.setUpdateBy(SecurityUtils.getUserId());
        //更新前的角色
        String roleKey = mongoTemplate.findById(role.getId(), Role.class).getRoleKey();
        //更新原有角色
        mongoTemplate.save(role);
        //删除原有菜单角色关联
        roleMenuService.removeRoleMenuByRoleId(role.getId());
        //更新最新菜单角色关联
        addRoleMenu(role);

        Set<Long> userIdsById = roleService.findUserIdsById(role.getId());

        for (  Long id : userIdsById) {
            LoginUser srcLoginUser =redisCache.getCacheObject("login:" + id);
            if (srcLoginUser != null) {
                //获取redis中的角色和权限信息

                Long[] menuIds = role.getMenuIds();
                //获取最新的menus权限
                List<String> parms = menuService.findMenuPamersList(menuIds);
                //更新最新的权限信息
                srcLoginUser.setPermissions(parms);
                //获取reids角色信息
                List<String> roleKeys = srcLoginUser.getUser().getRoles();
                for (int i = 0; i < roleKeys.size(); i++) {
                    if (roleKeys.get(i).equals(roleKey)) {
                        roleKeys.set(i, role.getRoleKey());
                    }
                }
                //更新最新角色信息
                srcLoginUser.getUser().setRoles(roleKeys);
                //更新redis中的认证信息
                redisCache.setCacheObject("login:" + id, srcLoginUser, 1800000);
            }
        }
    }

    @Override
    public Object updateById(Role role) {


        Update update = new Update();
        update.set("status",role.getStatus());
        UpdateResult result = mongoTemplate
                .updateFirst(Query
                        .query(Criteria.where("_id")
                                .is(role.getId())),update,Role.class);
        if (!result.wasAcknowledged()){
            return ResponseResult.errorResult(500,"状态修改失败");
        }

        return ResponseResult.okResult(200,"状态修改");
    }

    @Override
    public List<String> findRoleById(Long[] roleIds) {

            Query query = new Query();
            query.addCriteria(Criteria
                            .where("_id").in(roleIds))
                    .fields().include("role_key");
            List<Role> roleKey = mongoTemplate.find(query,Role.class);
            List<String> roleKeys = roleKey.stream().map(Role::getRoleKey).collect(Collectors.toList());
            return roleKeys;
        }

    @Override
    public Set<Long> findUserIdsById(Long roleId) {

        List<UserRole> userRoles = mongoTemplate
                .find(Query.query(Criteria.where("role_id").is(roleId)), UserRole.class);
        Set<Long> userIds = userRoles.stream().map(UserRole::getUserId).collect(Collectors.toSet());

        return userIds;


    }

    private List<RoleMenu> addRoleMenu(Role role) {
        List<RoleMenu> roleMenuList = Arrays.stream(role.getMenuIds())
                .map(memuId -> new RoleMenu(idManager.getMaxRoleId().incrementAndGet(), role.getId(), memuId))
                .collect(Collectors.toList());
        roleMenuService.addRoleMenuBatch(roleMenuList);
        return roleMenuList;
    }


    //    @Override
//    public ResponseResult selectRolePage(Role role, Integer pageNum, Integer pageSize) {
//        LambdaQueryWrapper<Role> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//        //目前没有根据id查询
////        lambdaQueryWrapper.eq(Objects.nonNull(role.getId()),Role::getId,role.getId());
//        lambdaQueryWrapper.like(StringUtils.hasText(role.getRoleName()),Role::getRoleName,role.getRoleName());
//        lambdaQueryWrapper.eq(StringUtils.hasText(role.getStatus()),Role::getStatus,role.getStatus());
//        lambdaQueryWrapper.orderByAsc(Role::getRoleSort);
//
//        Page<Role> page = new Page<>();
//        page.setCurrent(pageNum);
//        page.setSize(pageSize);
//        page(page,lambdaQueryWrapper);
//
//        //转换成VO
//        List<Role> roles = page.getRecords();
//
//        PageVo pageVo = new PageVo();
//        pageVo.setTotal(page.getTotal());
//        pageVo.setRows(roles);
//        return ResponseResult.okResult(pageVo);
//    }
//
//    @Override
//    @Transactional
//    public void insertRole(Role role) {
//        save(role);
//        System.out.println(role.getId());
//        if(role.getMenuIds()!=null&&role.getMenuIds().length>0){
//            insertRoleMenu(role);
//        }
//    }
//
//    @Override
//    public void updateRole(Role role) {
//        updateById(role);
//        roleMenuService.deleteRoleMenuByRoleId(role.getId());
//        insertRoleMenu(role);
//    }
//
//    @Override
//    public List<Role> selectRoleAll() {
//        return list(Wrappers.<Role>lambdaQuery().eq(Role::getStatus, SystemConstants.NORMAL));
//
//    @Override
//    public List<Long> selectRoleIdByUserId(Long userId) {
//        return getBaseMapper().selectRoleIdByUserId(userId);
//    }
//
//    private void insertRoleMenu(Role role) {
//        List<RoleMenu> roleMenuList = Arrays.stream(role.getMenuIds())
//                .map(memuId -> new RoleMenu(role.getId(), memuId))
//                .collect(Collectors.toList());
//        roleMenuService.saveBatch(roleMenuList);
//    }
    private List<Long> findRoleIdsByUserId(Long userId) {
        Aggregation aggregation = newAggregation(
                match(Criteria.where("user_id").is(userId)),
                lookup("sys_role", "role_id", "id", "roles"),
                unwind("roles"),
                project().and("roles.id").as("role_id")
        );

        AggregationResults<Long> results = mongoTemplate.aggregate(aggregation, "sys_user_role", Long.class);
        List<Long> mappedResults = results.getMappedResults();

        // 处理结果并返回需要的数据
        List<Long> roleIds = new ArrayList<>();
        for (Long result : mappedResults) {
            roleIds.add(result);
        }
        return roleIds;
    }

    private List<String> findRoleIdsByUserId(String userId) {

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("user_id").is(userId)),
                Aggregation.lookup("sys_role", "role_id", "id", "roles"),
                Aggregation.unwind("roles"),
                Aggregation.match(Criteria.where("roles.status").is("0")
                        .and("roles.del_flag")
                        .is("0")),
                Aggregation.project("roles.role_key").andExclude("_id")
        );

        AggregationResults<String> results = mongoTemplate.aggregate(aggregation, "sys_user_role", String.class);
        List<String> roleKeys = new ArrayList<>();
        for (String roleKey : results.getMappedResults()) {
            roleKeys.add(roleKey);
        }
        return roleKeys;
    }

    private List<String> findRoleKeyByUserId(Long userId) {
        //聚合查询
        Aggregation aggregation = Aggregation.newAggregation(
                /**
                 * form: 关联的对象集合
                 * local:主表关联字段
                 * foreign: 关联表关联字段
                 * as: 替换名称
                 *
                 */
                Aggregation.match(Criteria.where("user_id").is(userId)),
//                        .and("roles.status").is("0")),
//                        .and("roles.del_flag").is("0")),
                Aggregation.lookup("sys_role", "role_id", "_id", "roles"),
                Aggregation.project("roles.role_key")
        );

        AggregationResults<Map> sysUserRole = mongoTemplate.aggregate(aggregation, "sys_user_role", Map.class);
        List<Map> result = sysUserRole.getMappedResults();
        List<String> reRoleKeys = result.stream()
                .flatMap(item -> {
                    ArrayList<String> roleKeys = (ArrayList<String>) item.get("role_key");
                    return roleKeys.stream();
                })
                .collect(Collectors.toList());
        return reRoleKeys;
    }
}

