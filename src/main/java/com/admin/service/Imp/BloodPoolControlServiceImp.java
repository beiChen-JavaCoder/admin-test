package com.admin.service.Imp;

import com.admin.domain.ResponseResult;
import com.admin.domain.entity.BloodPoolControl;
import com.admin.domain.vo.GameControlVo;
import com.admin.domain.vo.PageVo;
import com.admin.enums.GameControlTypeEnum;
import com.admin.notification.Notification;
import com.admin.service.BloodPoolControlService;
import com.admin.utils.SecurityUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Xqf
 * @version 1.0
 */
@Service
@Slf4j
public class BloodPoolControlServiceImp implements BloodPoolControlService {


    @Autowired
    private Notification notification;

    @Override
    public ResponseResult getScore() {


        Long userId = SecurityUtils.getUserId();
        List<JSONObject> controlScoreNotification = notification.getGameNotification();


        PageVo pageVo = new PageVo();
        pageVo.setRows(controlScoreNotification);
        pageVo.setTotal(Long.valueOf(controlScoreNotification.size()));
        log.info("用户id：" + userId + "请求了血池控制列表：" + pageVo);
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult updateGame(GameControlVo gameControlVo) {
        //断言不为空
//        AssertUtils.notNull(gameControlVo.getScore(), AppHttpCodeEnum.SCORE_NOT_NULL);

        JSONObject controlGameType = new JSONObject();

        JSONObject controlGame = (JSONObject) gameControlVo.getGame();

        Integer controlType = gameControlVo.getType();

        if (controlType.equals(GameControlTypeEnum.bigVomitControl.getType())) {
            controlGameType = (JSONObject) controlGame.get("bigVomitControl");
            controlGameType.put("type", BloodPoolControl.UpdateType.bigVomit);
        } else if (controlType.equals(GameControlTypeEnum.vomitControl.getType())) {
            controlGameType = (JSONObject) controlGame.get("vomitControl");
            controlGameType.put("type", BloodPoolControl.UpdateType.vomit);
        } else if (controlType.equals(GameControlTypeEnum.bigEatControl.getType())) {
            controlGameType = (JSONObject) controlGame.get("bigEatControl");
            controlGameType.put("type", BloodPoolControl.UpdateType.bigEat);
        } else if (controlType.equals(GameControlTypeEnum.eatControl.getType())) {
            controlGameType = (JSONObject) controlGame.get("eatControl");
            controlGameType.put("type", BloodPoolControl.UpdateType.eat);
        } else {

            String.valueOf(controlGameType.put("score", gameControlVo.getScore()));
            // 获取字段值并进行正则表达式验证
            String regex = "^[+-]?(?!0+$)\\d{1,9}$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(gameControlVo.getScore());
            if (!matcher.matches()) {
                return ResponseResult.errorResult(500, "请按规则输入信息");
            }

        }
        controlGameType.put("gameId", controlGame.get("gameId"));
        String ratio = String.valueOf(controlGameType.get("ratio"));
        if (!StringUtils.hasText(ratio)) {
            // 获取字段值并进行正则表达式验证
            String regex = "^(?!0$)([1-9]\\d{0,3}|10000)$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(ratio);
            if (!matcher.matches()) {
                return ResponseResult.errorResult(500, "请按规则输入信息");
            }
        }

        String reMsg = notification.updateGameBloodNotification(controlGameType);

        if ("0".equals(reMsg)) {
            log.info("更新血池成功," + controlGameType);
            return ResponseResult.okResult(200, "血池更新成功");
        }
        log.error("更新血池失败," + gameControlVo);
        return ResponseResult.errorResult(500, "血池更新失败，请联系管理员");
    }

    @Override
    public ResponseResult refreshScore() {

        List<JSONObject> controlScoreNotification = notification.getGameNotification();

            //前端定时器type刷新血池分数
            JSONArray objects = new JSONArray();
        for (JSONObject jsonObject : controlScoreNotification) {
            HashMap<String, Long> hashMap = new HashMap<>();
            hashMap.put("bloodScore", Long.valueOf(String.valueOf(jsonObject.get("bloodScore"))));
            objects.add(hashMap);
        }
            PageVo pageVo = new PageVo();
            pageVo.setRows(objects);
            pageVo.setTotal(objects.stream().count());
            log.info("定时器血池分数刷新");
            return ResponseResult.okResult(pageVo);


    }
}
