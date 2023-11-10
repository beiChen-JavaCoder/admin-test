package com.admin.service;

import com.admin.domain.ResponseResult;
import com.admin.domain.entity.UserControl;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @author Xqf
 * @version 1.0
 */
public interface UserControlService {
    ResponseResult userControlList();

    ResponseResult updateUserControl(UserControl userControlVo);

    ResponseResult addUserControl(UserControl userControl);

    ResponseResult importRotbot(List<JSONObject> userControl);
}
