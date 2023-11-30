package com.admin.service;


import com.admin.domain.entity.RoleMenu;

import java.util.List;
import java.util.Set;

/**
 * @Author xqf
 */
public interface RoleMenuService {
    void addRoleMenuBatch(List<RoleMenu> roleMenuList);

    void removeRoleMenuByRoleId(Long id);

    /**
     * 根据roleid查询权限信息
     * @param roleIds
     * @return
     */
    Set<String> findMenuByRoleId(Long[] roleIds);
}