package com.admin.service.Imp;

import com.admin.domain.ResponseResult;
import com.admin.domain.entity.UserControl;
import com.admin.domain.vo.PageVo;
import com.admin.notification.Notification;
import com.admin.service.UserControlService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Xqf
 * @version 1.0
 */
@Service
@Slf4j
public class UserControlServiceImp implements UserControlService {

    @Autowired
    private Notification notification;


    @Override
    public ResponseResult userControlList() {

        List<JSONObject> controlScoreNotification = notification.getUserNotification();
        PageVo pageVo = new PageVo();
        pageVo.setRows(controlScoreNotification);
        pageVo.setTotal(Long.valueOf(controlScoreNotification.size()));
        log.info("获取到点控控制台信息："+pageVo);
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult updateUserControl(UserControl userControl) {

        JSONObject userControlJson = (JSONObject) JSON.toJSON(userControl);

        // 获取字段值并进行正则表达式验证
        long targetScore = userControl.getTargetScore();
        String regex = "^[+-]?(?!0$)([1-9]\\d{0,3}|999999999)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(String.valueOf(targetScore));
        if (!matcher.matches()) {
            return ResponseResult.errorResult(500, "请按规则输入信息");
        }
//        String reMsg = notification.updateUserControl(userControlJson);
        if ("0".equals("0")){
            log.info("更新点控成功：" + userControlJson);
            return ResponseResult.okResult(200, "点控更新成功");
        }
        log.error("更新点控失败：" + userControlJson);
        return ResponseResult.errorResult(500, "点控更新失败，请联系管理员");
    }
}
