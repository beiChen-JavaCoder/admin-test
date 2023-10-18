package com.admin.service;

import com.admin.domain.entity.User;
import org.springframework.stereotype.Service;


public interface UserService {

    /**
     * 根据用户名查用户
     * @return
     */

    public User findUserByUserName(String userName);
}
