package com.admin.service;

import com.admin.domain.entity.Role;

import java.util.List;

/**
 * @author Xqf
 * @version 1.0
 */
public interface RoleInfoService {

    List<Role> findRoleAll();

    List<Long> findRoleIdByUserId(Long userId);
}
