package com.admin.controller.system;

import com.admin.component.IdManager;
import com.admin.domain.ResponseResult;
import com.admin.domain.entity.MerchantBean;
import com.admin.domain.entity.MerchantEntity;
import com.admin.domain.entity.Role;
import com.admin.domain.entity.User;
import com.admin.domain.vo.MerchantVo;
import com.admin.domain.vo.UserAndMerchantVo;
import com.admin.domain.vo.UserInfoAndRoleIdsVo;
import com.admin.enums.AppHttpCodeEnum;
import com.admin.enums.UserTypeEnum;
import com.admin.exception.SystemException;
import com.admin.service.MerchantService;
import com.admin.service.RoleInfoService;
import com.admin.service.UserService;
import com.admin.utils.BeanCopyUtils;
import com.admin.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequestMapping("/system/user")
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
    @GetMapping("/list")
    public ResponseResult list(User user, Integer pageNum, Integer pageSize) {
        return userService.findUserPage(user, pageNum, pageSize);
    }

    /**
     * 新增用户
     */
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
    @PutMapping
    public ResponseResult edit(@RequestBody User user) {
        userService.updateUser(user);
        return ResponseResult.okResult();
    }
}