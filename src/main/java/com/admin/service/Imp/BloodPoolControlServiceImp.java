package com.admin.service.Imp;

import com.admin.domain.ResponseResult;
import com.admin.domain.entity.BloodPoolControl;
import com.admin.domain.entity.UserControl;
import com.admin.domain.vo.GameControlVo;
import com.admin.domain.vo.PageVo;
import com.admin.enums.AppHttpCodeEnum;
import com.admin.enums.GameControlTypeEnum;
import com.admin.notification.Notification;
import com.admin.service.BloodPoolControlService;
import com.admin.utils.AssertUtils;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

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

        List<JSONObject> controlScoreNotification = notification.getGameNotification();
        PageVo pageVo = new PageVo();
        pageVo.setRows(controlScoreNotification);
        pageVo.setTotal(Long.valueOf(controlScoreNotification.size()));
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult updateGame(GameControlVo gameControlVo) {
        //断言不为空
        AssertUtils.notNull(gameControlVo.getScore(), AppHttpCodeEnum.SCORE_NOT_NULL);


        JSONObject controlGameType = new JSONObject();


        JSONObject controlGame = (JSONObject) gameControlVo.getGame();

        Integer controlType = gameControlVo.getType();

        if (controlType.equals(GameControlTypeEnum.bigVomitControl.getType())) {
            controlGameType = (JSONObject) controlGame.get("bigVomitControl");
            controlGameType.put("type",BloodPoolControl.UpdateType.bigVomit);
        } else if (controlType.equals(GameControlTypeEnum.vomitControl.getType())) {
            controlGameType = (JSONObject) controlGame.get("vomitControl");
            controlGameType.put("type",BloodPoolControl.UpdateType.vomit);
        } else if (controlType.equals(GameControlTypeEnum.bigEatControl.getType())) {
            controlGameType = (JSONObject) controlGame.get("bigEatControl");
            controlGameType.put("type",BloodPoolControl.UpdateType.bigEat);
        } else if (controlType.equals(GameControlTypeEnum.eatControl.getType())) {
            controlGameType = (JSONObject) controlGame.get("eatControl");
            controlGameType.put("type",BloodPoolControl.UpdateType.eat);
        } else {

            String.valueOf(controlGameType.put("score", gameControlVo.getScore()));
            // 获取字段值并进行正则表达式验证
            String regex = "^[+-]?(?!0$)([1-9]\\d{0,3}|999999999)$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(gameControlVo.getScore());
            if (!matcher.matches()) {
                return ResponseResult.errorResult(500, "请按规则输入信息");
            }

        }
        controlGameType.put("gameId", controlGame.get("gameId"));
        String ratio = String.valueOf(controlGameType.get("ratio"));
        if (!StringUtils.hasText(ratio)){
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
}
