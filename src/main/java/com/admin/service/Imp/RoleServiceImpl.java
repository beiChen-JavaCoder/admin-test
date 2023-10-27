package com.admin.service.Imp;

import com.admin.constants.SystemConstants;
import com.admin.domain.entity.Role;
import com.admin.service.RoleMenuService;
import com.admin.service.RoleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
@Service("roleService")
public class RoleServiceImpl implements RoleService {

    @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    public List<String> selectRoleKeyByUserId(String id) {
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

