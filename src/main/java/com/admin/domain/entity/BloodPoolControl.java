package com.admin.domain.entity;

import lombok.Data;

/**
 * 血池控制参数
 * 获取血池信息请求地址:/control/getControlConfigs 无参数(post)
 * 血池信息:[{
 * "gameId":901,                                                    游戏编号
 * "gameName":"骰子",                                                游戏名称
 * "bloodScore":0,                                                  血池分数
 * "eatControl":{"limitScore":0,"ratio":5000},                      吃分控制
 * "bigEatControl”:{"limitScore":-10000,"ratio":7000},              狂吃控制
 * "vomitControl":{"limitScore":1000000000,"ratio":5000},           吐分控制
 * "bigVomitControl":{"limitScore":10000000000,"ratio":7000}        狂吐控制
 * }]
 * 修改血池信息请求地址:/control/updateBloodControl 本实例
 */
@Data
public class BloodPoolControl {
    public enum UpdateType{
        /** 吃分控制 */
        eat,
        /** 狂吃控制 */
        bigEat,
        /** 吐分控制 */
        vomit,
        /** 狂吐控制 */
        bigVomit
    }
    /** 修改血池时本值为空 */
    UpdateType type;
    /** 游戏编号 */
    long gameId;
    /** 修改血池分数 */
    long score;
    /** 开始控制的分数 */
    int limitScore;
    /** 控制概率 */
    int ratio;
}