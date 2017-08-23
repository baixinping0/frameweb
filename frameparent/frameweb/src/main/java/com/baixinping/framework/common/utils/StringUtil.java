package com.baixinping.framework.common.utils;

import org.apache.commons.lang3.StringUtils;

public class StringUtil {
    public static boolean isEmpty(String value){
        if (value != null)
            value = value.trim();
        return StringUtils.isEmpty(value);
    }
}
