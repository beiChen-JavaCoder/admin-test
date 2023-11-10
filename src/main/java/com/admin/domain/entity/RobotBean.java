package com.admin.domain.entity;

import lombok.Data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 机器人变更请求
 * 获取机器人控制数据地址: /robot/getRobotControls 无参数
 * 机器人控制信息:{
 * -- 机器人初始金币区间
 * "initScore":{"minScore":50000, "maxScore":200000000 },
 * -- 机器人下注区间
 * "betScore":{"minScore":10000, "maxScore":5000000 },
 * -- 机器人携带金币区间(存取区间:低于最小值存,高于最大值取)
 * "carryScore":{"minScore":10000, "maxScore":5000000000 },
 * -- 机器人下注时间区间(毫秒)
 * "betTime":{"min":500, "max":29000 },
 * -- 机器人下注概率(万分比)
 * "betRatio":10000
 * }
 *
 * 机器人控制变更地址:/robot/update 本实例
 */
@Data
public class RobotBean {
    public enum UpdateRobotType {
        /** 机器人初始金币区间 */
        initScore(0, "机器人初始金币区间"),
        /** 机器人下注区间 */
        betScore(1, "机器人下注区间"),
        /** 机器人携带金币区间(存取区间:低于最小值存,高于最大值取) */
        carryScore(2, "机器人携带金币区间(存取区间:低于最小值存,高于最大值取)"),
        /** 机器人下注时间区间(毫秒) */
        betTime(3, "机器人下注时间区间(毫秒)"),
        /** 机器人下注概率(万分比) */
        betRatio(4, "机器人下注概率(万分比)");
        public int num;
        public String remark;

        public int getNum() {
            return num;
        }

        static Map<Integer, UpdateRobotType> types = new HashMap<>();

        static {
            types = Arrays.stream(values()).collect(Collectors.toMap(UpdateRobotType::getNum, v -> v));
        }

        UpdateRobotType(int num, String remark) {
            this.num = num;
            this.remark = remark;
        }

        public static UpdateRobotType forNumber(int num) {
            return types.get(num);
        }
    }

    /** 修改类型(对应上面枚举) */
    int type;
    /** 机器人控制概率 */
    int betRatio;
    /** 最小值 */
    long min;
    /** 最大值 */
    long max;
}