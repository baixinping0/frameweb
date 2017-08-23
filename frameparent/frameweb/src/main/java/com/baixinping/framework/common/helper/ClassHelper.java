package com.baixinping.framework.common.helper;

import com.baixinping.framework.common.annotation.Controller;
import com.baixinping.framework.common.annotation.Service;
import com.baixinping.framework.common.utils.ClassUtil;
import com.baixinping.framework.common.utils.PropsUtil;
import com.sun.org.apache.bcel.internal.generic.ALOAD;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

public class ClassHelper {
    private static final Set<Class<?>> All_CLASS;
    static {
        All_CLASS = ClassUtil.getClassByPackage(ConfigHelper.getAppBasePackage());
    }

    public static Set<Class<?>> getAllClass(){
        return All_CLASS;
    }

    public static Set<Class<?>> getClassSetBySuper(Class<?> superClass){
        Set<Class<?>> set = new HashSet<Class<?>>();
        for (Class<?> clazz : set){
            if (clazz.isAssignableFrom(superClass) && !superClass.equals(clazz)){
                set.add(clazz);
            }
        }
        return set;
    }
    public static Set<Class<?>> getClassSetByAnnotation(Class<? extends Annotation> annotation){
        Set<Class<?>> set = new HashSet<Class<?>>();
        for (Class<?> clazz : set){
            if (clazz.isAnnotationPresent(annotation)){
                set.add(clazz);
            }
        }
        return set;
    }

    public static Set<Class<?>> getController(){
        Set<Class<?>> set = new HashSet<Class<?>>();
        for (Class<?> clazz : set){
            if (clazz.isAnnotationPresent(Controller.class)){
                set.add(clazz);
            }
        }
        return set;
    }

    public static Set<Class<?>> getService(){
        Set<Class<?>> set = new HashSet<Class<?>>();
        for (Class<?> clazz : set){
            if (clazz.isAnnotationPresent(Service.class)){
                set.add(clazz);
            }
        }
        return set;
    }
    public static Set<Class<?>> getBean(){
        Set<Class<?>> set = new HashSet<Class<?>>();
        set.addAll(getController());
        set.addAll(getService());
        return set;
    }
}
