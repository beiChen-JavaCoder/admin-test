package com.admin.controller.system;

import com.admin.annotation.Log;
import com.admin.domain.ResponseResult;
import com.admin.domain.dto.ChangeRoleStatusDto;
import com.admin.domain.entity.Menu;
import com.admin.domain.entity.Role;
import com.admin.enums.BusinessType;
import com.admin.service.Imp.MenuServiceImpl;
import com.admin.service.MenuService;
import com.admin.service.RoleService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/role")
@PreAuthorize("@ss.hasRole('admin')")
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
    @Log(title = "新增角色",businessType = BusinessType.INSERT)
    @ApiOperation("新增角色")
    @PostMapping
    public ResponseResult add(@RequestBody Role role) {
        roleService.addRole(role);
        return ResponseResult.okResult();

    }

    /**
     * 删除角色
     * @param id
     *
     */
    @ApiOperation("删除角色")
    @Log(title = "删除角色",businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public ResponseResult remove(@PathVariable(name = "id") Long id) {

        roleService.removeById(id);

        return ResponseResult.okResult();
    }
    /**
     * 修改保存角色
     */
    @ApiOperation("修改/保存角色")
    @Log(title = "修改/或保存角色",businessType = BusinessType.INSERT)
    @PutMapping
    public ResponseResult edit(@RequestBody Role role) {
        roleService.updateRole(role);
        return ResponseResult.okResult();
    }
    @ApiOperation("修改状态")
    @Log(title = "角色修改状态",businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public ResponseResult changeStatus(@RequestBody ChangeRoleStatusDto roleStatusDto) {
        Role role = new Role();
        role.setId(roleStatusDto.getRoleId());
        role.setStatus(roleStatusDto.getStatus());
        return ResponseResult.okResult(roleService.updateById(role));
    }
}
