package com.admin.service.Imp;

import com.admin.constants.Constants;
import com.admin.constants.UserConstants;
import com.admin.domain.ResponseResult;
import com.admin.domain.entity.LoginUser;
import com.admin.domain.entity.User;
import com.admin.domain.vo.UserInfoVo;
import com.admin.domain.vo.adminUserLoginVo;
import com.admin.enums.AppHttpCodeEnum;
import com.admin.exception.SystemException;
import com.admin.factory.AsyncFactory;
import com.admin.manager.AsyncManager;
import com.admin.security.context.AuthenticationContextHolder;
import com.admin.service.LoginService;
import com.admin.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LoginServiceImp implements LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult login(User user) {
        String userName = user.getUserName();
        String password = user.getPassword();
        // 验证码校验
//        validateCaptcha(username, code, uuid);
        // 登录前置校验
        loginPreCheck(userName, password);
        // 用户验证
        Authentication authentication = null;
        //判断认证信息
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
            AuthenticationContextHolder.setContext(authenticationToken);
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager.authenticate(authenticationToken);
        } catch (Exception e) {
            if (e instanceof BadCredentialsException) {
                //用户名或密码无效或不匹配错误
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(userName, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
                throw new SystemException(AppHttpCodeEnum.LOGIN_ERROR);
            } else {
                //系统内部错误
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(userName, Constants.LOGIN_FAIL, e.getMessage()));
                throw new SystemException(AppHttpCodeEnum.valueOf(e.getMessage()));
            }
        }
        finally
        {
            //清除当前线程认证上下文
            AuthenticationContextHolder.clearContext();
        }
        //异步存储当前用户登录日志信息
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(userName, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
//        recordLoginInfo(loginUser.getUser().getId());

        //获取userid 生成token
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);
        //把用户信息存入redis
        Integer timeOut = 1800000;
        redisCache.setCacheObject("login:" + userId, loginUser, timeOut);

        //把token和userinfo封装 返回
        //把User转换成UserInfoVo
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
        log.info("用户" + userId + "登录成功" + loginUser.getUser());
        adminUserLoginVo vo = new adminUserLoginVo(jwt, userInfoVo);
        return ResponseResult.okResult(vo);
    }

    @Override
    public ResponseResult logout() {
        //获取当前登录的用户id
        Long userId = SecurityUtils.getUserId();
        //删除redis中对应的值
        redisCache.deleteObject("login:" + userId);
        log.info("用户" + userId + "退出登录");
        return ResponseResult.okResult();
    }

    /**
     * 登录前置校验
     *
     * @param username 用户名
     * @param password 用户密码
     */
    public void loginPreCheck(String username, String password) {
        // 用户名或密码为空 错误
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("not.null")));
            throw new SystemException(AppHttpCodeEnum.LOGIN_ERROR);
        }
        // 密码如果不在指定范围内 错误
//        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
//                || password.length() > UserConstants.PASSWORD_MAX_LENGTH)
//        {
//            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
//            throw new UserPasswordNotMatchException();
//        }
        // 用户名不在指定范围内 错误
//        if (username.length() < UserConstants.USERNAME_MIN_LENGTH
//                || username.length() > UserConstants.USERNAME_MAX_LENGTH)
//        {
//            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
//            throw new UserPasswordNotMatchException();
//        }
        // IP黑名单校验
//        String blackStr = configService.selectConfigByKey("sys.login.blackIPList");
//        if (IpUtils.isMatchedIp(blackStr, IpUtils.getIpAddr()))
//        {
//            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("login.blocked")));
//            throw new BlackListException();
//        }
    }
    /**
     * 记录登录信息
     *
     * @param userId 用户ID
     */
//    public void recordLoginInfo(Long userId)
//    {
//        SysUser sysUser = new SysUser();
//        sysUser.setUserId(userId);
//        sysUser.setLoginIp(IpUtils.getIpAddr());
//        sysUser.setLoginDate(DateUtils.getNowDate());
//        userService.updateUserProfile(sysUser);
//    }
}
