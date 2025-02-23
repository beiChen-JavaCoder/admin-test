package com.admin.notification;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.admin.domain.dto.MerchantDto;
import com.admin.enums.MerchantTypeEnum;
import com.admin.utils.SecurityUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * 通知请求类
 *
 * @author Xqf
 * @version 1.0
 */
@Slf4j
@Component
public class Notification {


    @Value("${myconfig.game.ipPort}")
    private String ipPort;
    /**
     * 变更列表url
     */
    private String MERCHANT_LIST_CHANGE;
    /**
     * 商户充值变更url
     */
    private String MERCHANT_RECHARGE_CHANGE;
    /**
     * 商户提现变更url
     */
    private String MERCHANT_CASH_CHANGE;
    /**
     * 血池信息获取url
     */
    private String GET_CONTROL_CONFIGS;

    /**
     * 血池修改url
     */
    private String UPDATE_BLOOD_CONTROL;

    /**
     * 点控获取信息
     */
    private String POINT_CONTROL_GET;

    /**
     * 点控获取信息
     */
    private String POINT_CONTROL_UPDATE;
    /**
     * 新增点控对象
     */
    private String POINT_CONTROL_ADD;
    /**
     * 机器人信息
     */
    private String ROBOT_INFO_GET;
    /**
     * 机器人信息更新
     */
    private String ROBOT_INFO_UPDATE;
    /**
     * 机器人名称变更
     */
    private String ROBOT_NAME_UPDATEL;
    /**
     * 关闭游戏请求地址
     */
    private String TURN_GAME;
    /**
     * 删除游戏
     */
    private String DELETE_GAME;
    /**
     * 游戏列表
     */
    private String LIST_GAME;

    /**
     * 商户相关请求
     *
     * @param merchantDto
     * @return
     */
    public String notificationMerchant(MerchantDto merchantDto) {

        String url = null;

        // 将 MerchantBean 对象转换为 JSON 字符串
        String jsonString = JSON.toJSONString(merchantDto);


//        log.info("游戏服返回的结果:"+JSON.toJSONString(stringMono));
        if (merchantDto.getType() == MerchantTypeEnum.LIST.getType()) {
            url = MERCHANT_LIST_CHANGE;
        } else if (merchantDto.getType() == MerchantTypeEnum.CHARGE.getType()) {
            url = MERCHANT_RECHARGE_CHANGE;
        } else if (merchantDto.getType() == MerchantTypeEnum.CASH.getType()) {
            url = MERCHANT_CASH_CHANGE;
        }
        JSONObject parse = (JSONObject) JSON.parse(HttpUtil.post(url, jsonString));
        if (0 == (Integer) parse.get("errcode")) {
            return "0";
        }
        return "1";

    }

    /**
     * 获取血池信息
     *
     * @return null
     */
    public ArrayList<JSONObject> getGameNotification() {
        Object parse = JSONArray.parse(HttpUtil.post(GET_CONTROL_CONFIGS, ""));

        JSONArray jsonArray = JSONArray.parseArray(parse.toString());
        ArrayList<JSONObject> jsonObjects = new ArrayList<>();
        for (Object reGame : jsonArray) {
            jsonObjects.add((JSONObject) reGame);
        }
        log.info(jsonObjects.toString());

        if (StringUtils.hasText(parse.toString())) {
            return jsonObjects;
        }
        return null;
    }

    /**
     * 修改血池信息
     *
     * @param bloodPoolControl
     * @return
     */
    public String updateGameBloodNotification(JSONObject bloodPoolControl) {
        //血池type为空
        String jsonString = JSON.toJSONString(bloodPoolControl);
        JSONObject parse = (JSONObject) JSON.parse(HttpUtil.post(UPDATE_BLOOD_CONTROL, jsonString));
        if (0 == (Integer) parse.get("errcode")) {
            return "0";
        }
        return "1";
    }

    /**
     * 获取点控信息
     *
     * @return
     */
    public ArrayList<JSONObject> getUserNotification() {

        Object parse = JSONArray.parse(HttpUtil.post(POINT_CONTROL_GET, ""));

        JSONArray jsonArray = JSONArray.parseArray(parse.toString());
        ArrayList<JSONObject> jsonObjects = new ArrayList<>();
        for (Object reGame : jsonArray) {
            jsonObjects.add((JSONObject) reGame);
        }

        if (StringUtils.hasText(parse.toString())) {
            return jsonObjects;
        }
        return null;
    }

    /**
     * 修改点控信息
     *
     * @param userControl
     * @return
     */
    public String updateUserControl(JSONObject userControl) {

        String jsonString = JSON.toJSONString(userControl);
        JSONObject parse = (JSONObject) JSON.parse(HttpUtil.post(POINT_CONTROL_UPDATE, jsonString));
        if (0 == (Integer) parse.get("errcode")) {
            return "0";
        }
        return "1";
    }

    /**
     * 新增点控信息
     *
     * @param userControl
     * @return
     */
    public String addUserControl(JSONObject userControl) {

        String jsonString = JSON.toJSONString(userControl);
        JSONObject parse = (JSONObject) JSON.parse(HttpUtil.post(POINT_CONTROL_ADD, jsonString));
        if (0 == (Integer) parse.get("errcode")) {
            return "0";
        }
        return "1";
    }

    /**
     * 机器人列表请求
     *
     * @return
     */
    public List<JSONObject> getRobotList() {

        Object parse = JSONArray.parse(HttpUtil.post(ROBOT_INFO_GET, ""));

        JSONArray jsonArray = JSONArray.parseArray(parse.toString());
        ArrayList<JSONObject> jsonObjects = new ArrayList<>();
        for (Object reGame : jsonArray) {
            jsonObjects.add((JSONObject) reGame);
        }
        Long userId = SecurityUtils.getUserId();

        if (StringUtils.hasText(parse.toString())) {

            return jsonObjects;
        }
        return null;


    }

    /**
     * 修改机器人控制请求
     *
     * @param jsonObject
     * @return
     */
    public String updateRobotControl(JSONObject jsonObject) {

        String jsonString = JSON.toJSONString(jsonObject);
        JSONObject parse = (JSONObject) JSON.parse(HttpUtil.post(ROBOT_INFO_UPDATE, jsonString));
        if (0 == (Integer) parse.get("errcode")) {
            return "0";
        }
        return "1";
    }

    /**
     * 机器人名称变更请求
     */
    public String updateRobotName() {

        JSONObject parse = (JSONObject) JSON.parse(HttpUtil.post(ROBOT_NAME_UPDATEL, ""));
        log.info(parse.toString());
        if (0 == (Integer) parse.get("errcode")) {
            return "0";
        }
        return "1";
    }

    /**
     * 修改游戏（关闭删除）
     * type：0,关闭 1,删除
     *
     * @return 成功/失败
     */
    public boolean updateGame(Long gameId, Integer type) {

        String url = new String();
        if (type == 0) {
            url = TURN_GAME;
        } else if (type == 1) {
            url = DELETE_GAME;
        }

        // 发送POST请求
        HttpRequest request = HttpRequest.post(url)
                .body(gameId.toString())  // 将Long型数字转换为字符串并设置为请求体
                .header("Content-Type", "application/json")
                .timeout(3000);  // 设置请求头
        JSONObject body = (JSONObject) JSONObject.parse(request.execute().body());

        // 接收响应
        log.info(request.toString());
        if (0 == (Integer) body.get("errcode")) {
            return true;
        }
        return false;
    }

    public List<JSONObject> findGameList() {

        Object parse = JSONArray.parse(HttpUtil.post(LIST_GAME, ""));

        JSONObject jsonObject = (JSONObject) JSONObject.parse(parse.toString());
        JSONArray infos = JSONArray.parseArray(String.valueOf(jsonObject.get("infos")));
        ArrayList<JSONObject> jsonObjects = new ArrayList<>();
        for (Object reGame : infos) {
            jsonObjects.add((JSONObject) reGame);
        }

        return jsonObjects;


    }

    @PostConstruct
    public void init() {
        MERCHANT_LIST_CHANGE = "http://" + ipPort + "/hall/merchant/merchantListChange";
        MERCHANT_RECHARGE_CHANGE = "http://" + ipPort + "/hall/merchant/recharge";
        MERCHANT_CASH_CHANGE = "http://" + ipPort + "/hall/merchant/cash";
        GET_CONTROL_CONFIGS = "http://" + ipPort + "/control/getControlConfigs";
        UPDATE_BLOOD_CONTROL = "http://" + ipPort + "/control/updateBloodControl";
        POINT_CONTROL_GET = "http://" + ipPort + "/control/getUserControlConfigs";
        POINT_CONTROL_UPDATE = "http://" + ipPort + "/control/updateUserControl";
        POINT_CONTROL_ADD = "http://" + ipPort + "/control/updateUserControl";
        ROBOT_INFO_GET = "http://" + ipPort + "/robot/getRobotControls";
        ROBOT_INFO_UPDATE = "http://" + ipPort + "/robot/update";
        ROBOT_NAME_UPDATEL = "http://" + ipPort + "/robot/namesChange";
        TURN_GAME = "http://" + ipPort + "/system/gameClose";
        DELETE_GAME = "http://" + ipPort + "/system/dbClean";
        LIST_GAME = "http://" + ipPort + "/system/getGameInfos";


    }


}
