package com.baixinping.framework.common.helper;

import com.baixinping.framework.common.utils.ReflectUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class BeanHelper {
    public static final Map<Class<?>, Object> BEAN_MAP = new HashMap<Class<?>, Object>();

    static {
        Set<Class<?>> beanSet = ClassHelper.getBean();
        for (Class clazz : beanSet){
            BEAN_MAP.put(clazz, ReflectUtil.newInstance(clazz));
        }
    }

    public static Map<Class<?>, Object> getBeanMap(){
        return BEAN_MAP;
    }

    public static void setBean(Class<?> clazz, Object object){
        BEAN_MAP.put(clazz, object);
    }

    public static<T> T getBean(Class<T> clazz){
        if (!BEAN_MAP.containsKey(clazz))
            throw new RuntimeException("not get bean by class "+clazz);
        return (T) BEAN_MAP.get(clazz);
    }
}
