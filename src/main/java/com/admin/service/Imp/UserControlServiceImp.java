package com.admin.service.Imp;

import com.admin.domain.ResponseResult;
import com.admin.domain.vo.PageVo;
import com.admin.notification.Notification;
import com.admin.service.UserControlService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Xqf
 * @version 1.0
 */
@Service
public class UserControlServiceImp implements UserControlService {

    @Autowired
    private Notification notification;


    @Override
    public ResponseResult listPtp() {

        List<JSONObject> controlScoreNotification = notification.getUserNotification();
        PageVo pageVo = new PageVo();
        pageVo.setRows(controlScoreNotification);
        pageVo.setTotal(Long.valueOf(controlScoreNotification.size()));
        return ResponseResult.okResult(pageVo);
    }
}
