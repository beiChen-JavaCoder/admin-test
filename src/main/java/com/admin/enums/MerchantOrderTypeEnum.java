package com.admin.enums;

import lombok.Getter;
import org.omg.CORBA.TIMEOUT;

/**
 * @author Xqf
 * @version 1.0
 */

public enum MerchantOrderTypeEnum {

    UNTREATED(0,"待处理"),
    processed(1,"已处理"),
    REFUSE(2,"已拒绝"),
    PROCESSING_TIMEOUT(3,"处理超时");


    @Getter
    Integer type;
    @Getter
    String msg;

    MerchantOrderTypeEnum(Integer type,String msg){
        this.type=type;
        this.msg=msg;
    }
}
