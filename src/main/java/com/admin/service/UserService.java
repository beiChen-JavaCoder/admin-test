package com.admin.service;

import com.admin.domain.ResponseResult;
import com.admin.domain.entity.MerchantEntity;
import com.admin.domain.entity.User;
import com.admin.domain.vo.MerchantVo;
import com.admin.domain.vo.UserAndMerchantVo;

import java.util.List;


public interface UserService {

    /**
     * 根据用户名查用户
     * @return
     */
    public User findUserByUserName(String userName);

    ResponseResult findUserPage(User user, Integer pageNum, Integer pageSize);

    boolean checkUserNameUnique(String userName);

    ResponseResult addUser(UserAndMerchantVo userAndMerchantVo);

    User findUserById(Long userId);

    void removeByIds(List<Long> userIds);

    void updateUser(User user);

    /**
     * 更新状态
     * @param user
     * @return
     */
    Object updateById(User user);
}
