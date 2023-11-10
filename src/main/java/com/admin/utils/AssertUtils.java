package com.admin.utils;

import com.admin.enums.AppHttpCodeEnum;
import com.admin.exception.SystemException;

public class AssertUtils {

    public static void notNull(Object object, AppHttpCodeEnum appHttpCodeEnum) {
        if (object == null) {
            throw new SystemException(appHttpCodeEnum);
        }
    }
    public static void isNull(Object object, AppHttpCodeEnum appHttpCodeEnum) {
        if (object == null) {
            throw new SystemException(appHttpCodeEnum);
        }
    }

    public static void isTrue(boolean condition, AppHttpCodeEnum appHttpCodeEnum) {
        if (!condition) {
            throw new SystemException(appHttpCodeEnum);
        }
    }
    public static void notTrue(boolean condition, AppHttpCodeEnum appHttpCodeEnum) {
        if (!condition) {
            throw new SystemException(appHttpCodeEnum);
        }
    }


}
