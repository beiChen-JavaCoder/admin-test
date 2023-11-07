package com.admin.domain.entity;

import lombok.Data;

/**
 * 点控参数
 * 获取点控信息请求地址:/control/getUserControlConfigs 无参数
 * 点控信息:[
 * {"gameId":901,                   游戏编号
 * "gameName":"骰子",                游戏名称
 * "userControls":[                 点控列表
 * {"userId":1,                     玩家编号
 * "currentScore":0,                控制进度
 * "targetScore":10000}             目标分数
 * ]}]
 * 修改点控信息请求地址:/control/updateUserControl 本实例
 */
@Data
public class UserControl {
    /** 用户编号 */
    long userId;
    /** 游戏编号 */
    long gameId;
    /** 点控类型:1.新增,2.修改 */
    int type;
    /** 被控制的目标分数 */
    long targetScore;
}