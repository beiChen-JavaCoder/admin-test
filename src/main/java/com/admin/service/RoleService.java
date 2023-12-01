package com.admin.service;



import com.admin.domain.ResponseResult;
import com.admin.domain.entity.Role;
import com.admin.domain.entity.User;

import java.util.List;
import java.util.Set;


/**
 * 角色信息表(Role)表服务接口
 *
 * @author makejava
 * @since 2022-08-09 22:36:47
 */
public interface RoleService {

    List<String> selectRoleKeyByUserId(Long id);

    List<Role> findRoleAll();

    ResponseResult findRolePage(Role role, Integer pageNum, Integer pageSize);

    Role findRoleById(Long roleId);

    void addRole(Role role);

    void removeById(List<Long> ids);

    void updateRole(Role role);

    Object updateById(Role role);

    /**
     * 根據id查role_key
     * @param roleIds
     * @return
     */
    List<String> findRoleById(Long[] roleIds);

    /**
     * 根据角色id获取相关的绑定用户
     * @param id
     * @return
     */
    Set<Long> findUserIdsById(Long id);


//    ResponseResult selectRolePage(Role role, Integer pageNum, Integer pageSize);

//    void insertRole(Role role);
//
//    void updateRole(Role role);
//
//    List<Role> selectRoleAll();
//
//    List<Long> selectRoleIdByUserId(Long userId);
}

