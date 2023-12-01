package com.admin.controller.system;

import com.admin.annotation.Log;
import com.admin.domain.ResponseResult;
import com.admin.domain.dto.ChangeUserStatusDto;
import com.admin.domain.dto.BingUserMerchantDto;
import com.admin.domain.entity.MerchantEntity;
import com.admin.domain.entity.Role;
import com.admin.domain.entity.User;
import com.admin.domain.vo.UserAndMerchantVo;
import com.admin.domain.vo.UserInfoAndRoleIdsVo;
import com.admin.enums.AppHttpCodeEnum;
import com.admin.enums.BusinessType;
import com.admin.exception.SystemException;
import com.admin.service.MerchantService;
import com.admin.service.RoleInfoService;
import com.admin.service.UserService;
import com.admin.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api("用户模块")
@RestController
@RequestMapping("/system/user")
@PreAuthorize("@ss.hasRole('admin')")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleInfoService roleService;
    @Autowired
    private MerchantService merchantService;

    /**
     * 获取用户列表
     */
    @ApiOperation("用户列表")
    @GetMapping("/list")
    public ResponseResult list(User user, Integer pageNum, Integer pageSize) {
        return userService.findUserPage(user, pageNum, pageSize);
    }

    /**
     * 新增用户
     */
    @ApiOperation("新增用户")
    @Log(title = "新增用户",businessType = BusinessType.INSERT)
    @PostMapping
    public ResponseResult add(@RequestBody UserAndMerchantVo userAndMerchantVo) {

        if (!userService.checkUserNameUnique(userAndMerchantVo.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
            userService.addUser(userAndMerchantVo);

        return ResponseResult.okResult();
    }

    /**
     * 根据用户编号获取详细信息
     */
    @ApiOperation("用户详情")
    @GetMapping(value = {"/{userId}"})
    public ResponseResult getUserInfoAndRoleIds(@PathVariable(value = "userId") Long userId) {
        List<Role> roles = roleService.findRoleAll();
        User user = userService.findUserById(userId);
        MerchantEntity merchantEntity = merchantService.findMerchantByUserId(userId);
        //当前用户所具有的角色id列表
        List<Long> roleIds = roleService.findRoleIdByUserId(userId);
        UserInfoAndRoleIdsVo vo = new UserInfoAndRoleIdsVo(user,merchantEntity,roles, roleIds);
        return ResponseResult.okResult(vo);
    }

    /**
     * 删除用户
     */
    @ApiOperation("删除用户")
    @Log(title = "删除用户",businessType = BusinessType.DELETE)
    @DeleteMapping("/{userIds}")
    public ResponseResult remove(@PathVariable List<Long> userIds) {
        if (userIds.contains(SecurityUtils.getUserId())) {
            return ResponseResult.errorResult(500, "不能删除当前你正在使用的用户");
        }
        userService.removeByIds(userIds);
        return ResponseResult.okResult();
    }

    /**
     * 修改用户
     */
    @ApiOperation("修改用户")
    @Log(title = "修改用户",businessType = BusinessType.UPDATE)
    @PutMapping
    public ResponseResult edit(@RequestBody User user) {
        userService.updateUser(user);
        return ResponseResult.okResult();
    }
    @ApiOperation("修改状态")
    @Log(title = "用户修改状态",businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public ResponseResult changeStatus(@RequestBody ChangeUserStatusDto userStatusDto) {

        return ResponseResult.okResult(userService.updateById(userStatusDto));
    }
    @Log(title = "绑定商户",businessType = BusinessType.UPDATE)
    @ApiOperation("绑定商户")
    @PutMapping("/binding")
    public ResponseResult bingMerchant(@RequestBody @Validated BingUserMerchantDto userMerchantDto){



        if (userMerchantDto.getUserId()==null&&userMerchantDto.getMerchantId()==null) {

        return ResponseResult.errorResult(500,"商户id不能为空");
        }

        return userService.bindingMerchant(userMerchantDto);


    }
}