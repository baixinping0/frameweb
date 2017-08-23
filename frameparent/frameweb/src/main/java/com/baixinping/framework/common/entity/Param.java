package com.baixinping.framework.common.entity;

import java.util.Map;

public class Param {
    private Map<String, Object> paramMap;

    public Param(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }

    public String getString(String key){
        return (String) paramMap.get(key);
    }

    public Map<String, Object> getParamMap() {
        return paramMap;
    }
}
