package com.admin.domain.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Xqf
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerVo {

    //玩家id
    private long rid;
    //注册时间
    private String[] regTime;
    //最后登陆时间
    private String[] lastlogin_time;
    //金币范围
    private JSONObject gold;
    //输赢区间
    private JSONObject winningLosing;
    //注册时间
    private JSONObject recharge;

    private Integer pageNum;
    private Integer isOnline;

    private Integer pageSize;
}
