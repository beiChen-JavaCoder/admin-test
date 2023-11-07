package com.admin.enums;

import lombok.Getter;

/**
 * @author Xqf
 * @version 1.0
 */
public enum GameControlTypeEnum {

    /**
     * 1:狂吐，2:吐分，3:狂吃，4:吃分
     */
    bigVomitControl(1,"狂吐控制"),
    vomitControl(2,"吐分控制"),
    bigEatControl(3,"狂吃控制"),
    eatControl(4,"吃分控制");
    @Getter
    private Integer type;
    @Getter
    private String msg;

    GameControlTypeEnum(Integer type, String msg) {
        this.type = type;
        this.msg = msg;
    }
}
