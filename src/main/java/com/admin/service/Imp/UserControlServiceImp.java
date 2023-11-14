package com.admin.service.Imp;

import com.admin.domain.ResponseResult;
import com.admin.domain.entity.UserControl;
import com.admin.domain.vo.PageVo;
import com.admin.notification.Notification;
import com.admin.service.UserControlService;
import com.admin.utils.SecurityUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
        Long userId = SecurityUtils.getUserId();
        List<JSONObject> controlScoreNotification = notification.getUserNotification();
        // 对controlScoreNotification中的每个JSONObject对象进行处理
        controlScoreNotification.forEach(game -> {
            JSONArray userControls = game.getJSONArray("userControls"); // 假设内部数组的属性名为"yourArrayPropertyName"
            if (userControls != null) {
                List<JSONObject> sortedList = userControls.stream()
                        .map(userControl -> (JSONObject) userControl) // 将JSONArray中的元素转换为JSONObject
                        .sorted(Comparator.comparingLong(o -> o.getLong("time"))) // 按照时间属性排序
                        .collect(Collectors.toList());

                // 将排序后的列表重新放回原来的JSONObject对象中
                game.put("userControls", sortedList);
            }
        });

        PageVo pageVo = new PageVo();
        pageVo.setRows(controlScoreNotification);
        pageVo.setTotal(Long.valueOf(controlScoreNotification.size()));
        log.info("用户id："+userId+"获取到点控控制台信息：" + pageVo);
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
        String reMsg = notification.updateUserControl(userControlJson);
        if ("0".equals(reMsg)) {
            log.info("更新点控成功：" + userControlJson);
            return ResponseResult.okResult(200, "点控更新成功");
        }
        log.error("更新点控失败：" + userControlJson);
        return ResponseResult.errorResult(500, "点控更新失败");
    }

    @Override
    public ResponseResult addUserControl(UserControl userControl) {
        JSONObject userControlJson = (JSONObject) JSONObject.toJSON(userControl);
        Long userId = SecurityUtils.getUserId();
        String remsg = notification.addUserControl(userControlJson);
        if ("0".equals(remsg)) {
            log.info("用户id："+userId+"新增点控成功"+userControl);
            return ResponseResult.okResult(200, "新增点控成功");
        }
        log.error("用户id："+userId+"新增点控失败"+userControl);
        return ResponseResult.errorResult(500, "新增点控失败");
    }

    @Override
    public ResponseResult importRotbot(List<JSONObject> userControl) {

        return null;
    }
}
