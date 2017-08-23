package com.baixinping.framework.common.loader;

import com.baixinping.framework.common.helper.*;
import com.baixinping.framework.common.utils.ClassUtil;

public class HelperLoader {
    public static void init(){
        Class<?>[] helperList = new Class[]{
                BeanHelper.class,
                ClassHelper.class,
                ControllerHelper.class,
                AopHelper.class,
                IocHelper.class
                };
        for (Class<?> clazz : helperList){
            ClassUtil.loadClass(clazz.getName(), true);
        }
    }
}
