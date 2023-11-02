package com.admin.controller;

import com.admin.domain.ResponseResult;
import com.admin.domain.entity.Role;
import com.admin.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/role")
public class RoleController {


    @Autowired
    private RoleService roleService;

    @GetMapping("/list")
    public ResponseResult list(Role role, Integer pageNum, Integer pageSize) {
        return roleService.findRolePage(role, pageNum, pageSize);
    }
    @GetMapping("/listAllRole")
    public ResponseResult listAllRole() {
        List<Role> roles = roleService.findRoleAll();
        return ResponseResult.okResult(roles);
    }
    /**
     * 根据角色编号获取详细信息
     */
    @GetMapping(value = "/{roleId}")
    public ResponseResult getInfo(@PathVariable Long roleId) {
        Role role = roleService.findRoleById(roleId);
        return ResponseResult.okResult(role);
    }
    /**
     * 新增角色
     */
    @PostMapping
    public ResponseResult add(@RequestBody Role role) {
        roleService.addRole(role);
        return ResponseResult.okResult();

    }
}
