package com.baixinping.framework.common.helper;

import com.baixinping.framework.common.annotation.Resource;
import com.baixinping.framework.common.utils.ReflectUtil;
import org.apache.commons.collections4.MapUtils;

import java.lang.reflect.Field;
import java.util.Map;

public final class IocHelper {

    static {
        Map<Class<?>, Object> beanMap = BeanHelper.getBeanMap();
        if (!MapUtils.isEmpty(beanMap)){
            for (Map.Entry<Class<?>, Object> entry : beanMap.entrySet()){
                Class<?> clazz = entry.getKey();
                Object obj = entry.getValue();
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields){
                    if(field.isAnnotationPresent(Resource.class)){
                        //获取当前子段所对应的beanclass
                        Class<?> type = field.getType();
                        Object beanFieldInstance = BeanHelper.getBean(type);
                        if (beanFieldInstance != null)
                            ReflectUtil.setField(obj, field, beanFieldInstance);
                    }
                }
            }
        }
    }
}
