package com.admin.enums;

import lombok.Getter;

/**
 * @author Xqf
 * @version 1.0
 */
public enum UserStatusEnum {
    DEACTIVATE("1","停用"),
    NORMAL("0","正常");


    @Getter
    String code;
    @Getter
    String msg;

    UserStatusEnum(String code, String Message) {
        this.code = code;
        this.msg = Message;
    }




}
