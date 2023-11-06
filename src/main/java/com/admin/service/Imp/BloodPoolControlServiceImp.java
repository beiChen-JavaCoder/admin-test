package com.admin.service.Imp;

import com.admin.domain.ResponseResult;
import com.admin.domain.vo.PageVo;
import com.admin.notification.Notification;
import com.admin.service.BloodPoolControlService;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Xqf
 * @version 1.0
 */
@Service
@Slf4j
public class BloodPoolControlServiceImp implements BloodPoolControlService {

    @Autowired
    private BloodPoolControlService bloodPoolControlService;
    @Autowired
    private Notification notification;

    @Override
    public ResponseResult getScore() {

        List<JSONObject> controlScoreNotification = notification.getControlScoreNotification();
        PageVo pageVo = new PageVo();
        pageVo.setRows(controlScoreNotification);
        pageVo.setTotal(Long.valueOf(controlScoreNotification.size()));
        return ResponseResult.okResult(pageVo);
    }
}
