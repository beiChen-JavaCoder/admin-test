package com.admin.service.Imp;

import com.admin.domain.ResponseResult;
import com.admin.domain.entity.LoginUser;
import com.admin.domain.entity.User;
import com.admin.domain.vo.UserInfoVo;
import com.admin.domain.vo.adminUserLoginVo;
import com.admin.enums.AppHttpCodeEnum;
import com.admin.exception.SystemException;
import com.admin.service.LoginService;
import com.admin.utils.BeanCopyUtils;
import com.admin.utils.JwtUtil;
import com.admin.utils.RedisCache;
import com.admin.utils.SecurityUtils;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.logging.Logger;

@Service
@Slf4j
public class LoginServiceImp implements LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;
    @Override
    public ResponseResult login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //判断是否认证通过
        if(Objects.isNull(authenticate)){
            log.error("用户"+user.getUserName()+"尝试登录失败"+user);
            throw new SystemException(AppHttpCodeEnum.LOGIN_ERROR);

        }
        //获取userid 生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);
        //把用户信息存入redis
        Integer timeOut = 1800000;
        redisCache.setCacheObject("login:"+userId,loginUser,timeOut);

        //把token和userinfo封装 返回
        //把User转换成UserInfoVo
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
        log.info("用户"+userId+"登录成功"+loginUser.getUser());
        adminUserLoginVo vo = new adminUserLoginVo(jwt,userInfoVo);
        return ResponseResult.okResult(vo);
    }

    @Override
    public ResponseResult logout() {
        //获取当前登录的用户id
        Long userId = SecurityUtils.getUserId();
        //删除redis中对应的值
        redisCache.deleteObject("login:"+userId);
        log.info("用户"+userId+"退出登录");
        return ResponseResult.okResult();
    }
}
