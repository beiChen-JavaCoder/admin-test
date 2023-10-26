package com.admin.controller;

import com.admin.domain.ResponseResult;
import com.admin.domain.entity.LoginUser;
import com.admin.domain.entity.Menu;
import com.admin.domain.vo.RoutersVo;
import com.admin.domain.entity.User;
import com.admin.domain.vo.AdminUserInfoVo;
import com.admin.domain.vo.UserInfoVo;
import com.admin.enums.AppHttpCodeEnum;
import com.admin.exception.SystemException;
import com.admin.service.LoginService;
import com.admin.service.MenuService;
import com.admin.service.RoleService;
import com.admin.utils.BeanCopyUtils;
import com.admin.utils.SecurityUtils;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/admin")
@Api(tags = "登录注销模块")
public class LoginController {

    @Autowired
    private LoginService loginService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private RoleService roleService;



    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user) {
        if (!StringUtils.hasText(user.getUserName())) {
            //提示 必须要传用户名
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return loginService.login(user);
    }

    @PostMapping("/user/logout")
    public ResponseResult logout() {
        return loginService.logout();
    }

    @GetMapping("getInfo")
    public ResponseResult getInfo() {
        //获取当前登录的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        //根据用户id查询权限信息
        List<String> perms = menuService.selectPermsByUserId(loginUser.getUser().getId());
        //根据用户id查询角色信息
        List<String> roleKeyList = roleService.selectRoleKeyByUserId(loginUser.getUser().getId());
        //获取用户信息
        User user = loginUser.getUser();
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        //封装数据返回

        AdminUserInfoVo adminUserInfoVo = new AdminUserInfoVo(perms,roleKeyList,userInfoVo);
        System.out.println(adminUserInfoVo.toString());
        return ResponseResult.okResult(adminUserInfoVo);


    }
    @GetMapping("getRouters")
    public ResponseResult<RoutersVo> getRouters(){
        String userId = SecurityUtils.getUserId();
        //查询menu 结果是tree的形式
        List<Menu> menus = menuService.selectRouterMenuTreeByUserId(userId);
        //封装数据返回
        System.out.println(new RoutersVo(menus));
        return ResponseResult.okResult(new RoutersVo(menus));
    }
}
