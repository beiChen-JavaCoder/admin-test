package com.admin.enums;

import lombok.Getter;

/**
 * @author Xqf
 * @version 1.0
 */

public enum OrderAccountTypeEnum {


    WECHAT(1, "微信"),
    ALIPAY(2, "支付宝"),

    Bank(3, "银行卡");


    @Getter
    private Integer type;
    @Getter
    private String msg;


    OrderAccountTypeEnum(Integer type, String msg) {
        this.type = type;
        this.msg = msg;
    }
}
