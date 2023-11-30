package com.admin.service.Imp;

import com.admin.domain.entity.LoginUser;
import com.admin.domain.entity.User;
import com.admin.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

    @Autowired
    private UserService userService;
    @Autowired
    private MenuService menuService;

    @Autowired
    private RoleService roleService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名查询用户信息
        User user = userService.findUserByUserName(username);
        //判断是否查到用户  如果没查到抛出异常
        if(Objects.isNull(user)){
            throw new RuntimeException("用户不存在");
        }
        //角色信息（用于权限验证）
        List<String> roleKeyList = roleService.selectRoleKeyByUserId(user.getId());
        user.setRoles(roleKeyList);
        //权限信息
        List<String> perms = menuService.selectPermsByUserId(user.getId());
        if (roleKeyList.isEmpty()||perms.isEmpty()){
            throw new RuntimeException("无权限，请联系管理员");
        }
        //返回用户信息
        return new LoginUser(user,perms);
    }
}
