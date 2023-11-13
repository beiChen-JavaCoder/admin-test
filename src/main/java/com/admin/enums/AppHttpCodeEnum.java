package com.admin.enums;

public enum AppHttpCodeEnum {
    // 成功
    SUCCESS(200,"操作成功"),
    // 登录
    NEED_LOGIN(401,"需要登录后操作"),
    NO_OPERATOR_AUTH(403,"无权限操作"),
    SYSTEM_ERROR(500,"出现错误"),
    USERNAME_EXIST(501,"用户名已存在"),
     PHONENUMBER_EXIST(502,"手机号已存在"), EMAIL_EXIST(503, "邮箱已存在"),
    REQUIRE_USERNAME(504, "必需填写用户名"),
    USERNAME_NOT_NULL(508, "用户名不能为空"),
    NICKNAME_NOT_NULL(509, "昵称不能为空"),
    PASSWORD_NOT_NULL(510, "密码不能为空"),
    EMAIL_NOT_NULL(511, "邮箱不能为空"),
    NICKNAME_EXIST(512, "昵称已存在"),
    USER_DUPLICATION(513,"用户存在多个"),
    LOGIN_ERROR(505,"用户名或密码错误"),
    MERCHANT_ORDER_OK(506,"商户提现订单提交成功"),
    MERCHANT_ORDER_NO(506,"商户提现订单提交失败"),
    MERCHANT_REMOVE_NO(507,"对商户进行修改失败"),
    NOTIFICATION_NO(508,"变更列表通知失败"),
    RECHARGE_NO(509,"充值对象不存在"),
    ADD_USER_MERCHANT_NO(510,"添加用户商户或绑定商户失败"),
    NOTIFICATION_FAILURE(1,"通知失败"),
    NOTIFICATION_SUCCESS(0,"通知成功"),
    SCORE_NOT_NULL(511,"血池分数不能为空"),
    UPLOAL_ROBOT_NAME(512,"上传的机器人已存在");

    int code;
    String msg;

    AppHttpCodeEnum(int code, String errorMessage){
        this.code = code;
        this.msg = errorMessage;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
