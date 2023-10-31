package com.admin.service;

import com.admin.domain.ResponseResult;
import com.admin.domain.entity.User;
import com.admin.domain.vo.MerchantVo;

import java.util.List;


public interface UserService {

    /**
     * 根据用户名查用户
     * @return
     */

    public User findUserByUserName(String userName);

    ResponseResult findUserPage(User user, Integer pageNum, Integer pageSize);

    boolean checkUserNameUnique(String userName);

    ResponseResult addUser(User user);

    User findUserById(Long userId);

    void removeByIds(List<Long> userIds);

    void updateUser(User user);
}
