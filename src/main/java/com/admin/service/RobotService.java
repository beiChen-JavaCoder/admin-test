package com.admin.service;

import com.admin.domain.ResponseResult;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @author Xqf
 * @version 1.0
 */
public interface RobotService {
    public ResponseResult importRobot(List<JSONObject> robot);
}
