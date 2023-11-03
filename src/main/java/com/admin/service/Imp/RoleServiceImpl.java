package com.admin.service.Imp;

import cn.hutool.core.date.DateUtil;
import com.admin.component.IdManager;
import com.admin.constants.SystemConstants;
import com.admin.domain.ResponseResult;
import com.admin.domain.entity.Role;
import com.admin.domain.entity.RoleMenu;
import com.admin.domain.vo.PageVo;
import com.admin.service.RoleMenuService;
import com.admin.service.RoleService;
import com.admin.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2022-08-09 22:36:47
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private RoleMenuService roleMenuService;
    @Autowired
    private IdManager idManager;


    @Override
    public List<String> selectRoleKeyByUserId(Long id) {
        //判断是否是管理员 如果是返回集合中只需要有admin

        List<String> roleKeys = new ArrayList<>();
        roleKeys.add("admin");
        return roleKeys;
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
    public void removeById(Long id) {
        //删除角色信息
        mongoTemplate.remove(Query.query(Criteria.where("_id").is(id)), Role.class);
        //删除角色绑定的菜单信息
        roleMenuService.removeRoleMenuByRoleId(id);
    }

    @Override
    public void updateRole(Role role) {

        role.setUpdateTime(DateUtil.date());
        role.setUpdateBy(SecurityUtils.getUserId());
        //更新原有角色
        mongoTemplate.save(role);
        //删除原有菜单角色关联
        roleMenuService.removeRoleMenuByRoleId(role.getId());
        //更新最新菜单角色关联
        addRoleMenu(role);


    }

    private void addRoleMenu(Role role) {
        List<RoleMenu> roleMenuList = Arrays.stream(role.getMenuIds())
                .map(memuId -> new RoleMenu(idManager.getMaxRoleId().incrementAndGet(),role.getId(), memuId))
                .collect(Collectors.toList());
        roleMenuService.addRoleMenuBatch(roleMenuList);
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
//    }
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
}

