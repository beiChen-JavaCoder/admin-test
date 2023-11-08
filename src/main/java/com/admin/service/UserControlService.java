package com.admin.service;

import com.admin.domain.ResponseResult;
import com.admin.domain.entity.UserControl;

/**
 * @author Xqf
 * @version 1.0
 */
public interface UserControlService {
    ResponseResult userControlList();

    ResponseResult updateUserControl(UserControl userControlVo);
}
