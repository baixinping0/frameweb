package com.baixinping.framework.common.utils;

import com.sun.org.apache.bcel.internal.generic.LOOKUPSWITCH;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

public class ReflectUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReflectUtil.class);

    public static <T> T newInstance(Class<T> clazz){
        T obj = null;
        try {
            obj = clazz.newInstance();
        } catch (Exception e) {
            LOGGER.error("create instance failure", e);
            throw new RuntimeException(e);
        }
        return obj;
    }

    public static Object invokeMethod(Object obj, Method method, Object...params){
        Object result = null;
        method.setAccessible(true);
        try {
            method.invoke(obj, params);
        } catch (Exception e) {
           LOGGER.error("invoke method failure", e);
           throw new RuntimeException(e);
        }
        return  result;
    }

    public static void setField(Object obj, Field field, Object value){
        field.setAccessible(true);
        try {
            field.set(obj, value);
        } catch (IllegalAccessException e) {
            LOGGER.error("set field failure", e);
            throw new RuntimeException(e);
        }
    }

}
