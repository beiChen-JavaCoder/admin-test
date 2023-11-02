package com.admin.service;


import com.admin.domain.entity.RoleMenu;

import java.util.List;

/**
 * @Author xqf
 */
public interface RoleMenuService {
    void addRoleMenuBatch(List<RoleMenu> roleMenuList);

//    void deleteRoleMenuByRoleId(Long id);
}