package com.admin.service;



import java.util.List;


/**
 * 角色信息表(Role)表服务接口
 *
 * @author makejava
 * @since 2022-08-09 22:36:47
 */
public interface RoleService {

    List<String> selectRoleKeyByUserId(String id);

//    ResponseResult selectRolePage(Role role, Integer pageNum, Integer pageSize);

//    void insertRole(Role role);
//
//    void updateRole(Role role);
//
//    List<Role> selectRoleAll();
//
//    List<Long> selectRoleIdByUserId(Long userId);
}

