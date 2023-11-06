package com.admin.notification;

import cn.hutool.http.HttpUtil;
import com.admin.domain.dto.MerchantDto;
import com.admin.enums.MerchantTypeEnum;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;

/**
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
     * 点控信息获取url
     */
    private String UPDATE_BLOOD_CONTROL;

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
        if (0==(Integer) parse.get("errcode")) {
            return "0";
        }
        return "1";

    }

    /**
     * 获取血池信息
     *
     * @return null
     */
    public ArrayList<JSONObject> getControlScoreNotification() {
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

    @PostConstruct
    public void init() {
        MERCHANT_LIST_CHANGE = "http://" + ipPort + "/hall/merchant/merchantListChange";
        MERCHANT_RECHARGE_CHANGE = "http://" + ipPort + "hall/merchant/recharge";
        MERCHANT_CASH_CHANGE = "http://" + ipPort + "/hall/merchant/cash";
        GET_CONTROL_CONFIGS = "http://" + ipPort + "/control/getControlConfigs";
        UPDATE_BLOOD_CONTROL = "http://" + ipPort + "/control/updateBloodControl";
    }
}
