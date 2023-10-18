package com.admin.enums;

/**
 *用户类型：0代表普通用户，1代表管理员
 */
public enum UserTypeEnum {

    admin("1","管理员"),
    common("2","普通用户");


    String code;
    String msg;

    UserTypeEnum(String code, String Message) {
        this.code = code;
        this.msg = Message;
    }
    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }





}
