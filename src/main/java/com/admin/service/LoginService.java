package com.admin.service;

import com.admin.domain.ResponseResult;
import com.admin.domain.entity.User;
import org.springframework.stereotype.Service;


public interface LoginService {

    ResponseResult login(User user);

    ResponseResult logout();

}
