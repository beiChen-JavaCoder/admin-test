package com.admin.enums;

import lombok.Getter;

/**
 * @author Xqf
 * @version 1.0
 */

public enum MerchantTypeEnum {

    LIST(1,"商户列表变更"),
    CHARGE(2,"充值"),
    CASH(3,"提现");


    @Getter
    Integer type;
    @Getter
    String msg;

    MerchantTypeEnum(Integer type,String msg){
        this.type=type;
        this.msg=msg;
    }
    }
