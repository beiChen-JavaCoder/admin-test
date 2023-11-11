package com.admin.domain.entity;

import lombok.Data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 机器人变更请求
 * 机器人名称变更:/robot/namesChange 无参数
 * 获取机器人控制数据地址: /robot/getRobotControls 无参数
 * 机器人控制信息:[{
 * "gameId":901
 * "gameName":"骰子"
 * -- 机器人初始金币区间
 * "initScore":{"min":50000, "max":200000000 },
 * -- 机器人下注区间
 * "betScore":{"min":10000, "max":5000000 },
 * -- 机器人携带金币区间(存取区间:低于最小值存,高于最大值取)
 * "carryScore":{"min":10000, "max":5000000000 },
 * -- 进入房间机器人数量
 * "inRoomNum":{"min":10, "max":20 },
 * -- 机器人下注时间区间(毫秒)
 * "betTime":{"min":500, "max":29000 },
 * -- 机器人下注概率(万分比)
 * "betRatio":10000
 * }]
 *
 * 机器人控制变更地址:/robot/update 本实例
 */
@Data
public class RobotBean {
    public enum Type {
        /** 机器人初始金币区间 */
        initScore(0, "机器人初始金币区间"),
        /** 机器人下注区间 */
        betScore(1, "机器人下注区间"),
        /** 机器人携带金币区间(存取区间:低于最小值存,高于最大值取) */
        carryScore(2, "机器人携带金币区间(存取区间:低于最小值存,高于最大值取)"),
        /** 机器人下注时间区间(毫秒) */
        betTime(3, "机器人下注时间区间(毫秒)"),
        /** 机器人下注概率(万分比) */
        betRatio(4, "机器人下注概率(万分比)"),
        /** 进入房间机器人数量 */
        inRoomNum(5,"进入房间机器人数量");
        public int num;
        public String remark;

        public int getNum() {
            return num;
        }

        static Map<Integer, Type> types = new HashMap<>();

        static {
            types = Arrays.stream(values()).collect(Collectors.toMap(Type::getNum, v -> v));
        }

        Type(int num, String remark) {
            this.num = num;
            this.remark = remark;
        }

        public static Type forNumber(int num) {
            return types.get(num);
        }
    }

    /** 修改类型(对应上面枚举) */
    int type;
    /** 机器人控制概率 */
    int betRatio;
    /** 游戏编号 */
    long gameId;
    /** 最小值 */
    long min;
    /** 最大值 */
    long max;
}