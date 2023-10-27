package com.admin.controller;

import com.admin.domain.ResponseResult;
import com.admin.domain.entity.Role;
import com.admin.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/system/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/listAllRole")
    public ResponseResult listAllRole() {
        List<Role> roles = roleService.findRoleAll();
        return ResponseResult.okResult(roles);
    }


}