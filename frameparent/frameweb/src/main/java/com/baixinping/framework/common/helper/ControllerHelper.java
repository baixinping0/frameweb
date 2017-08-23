package com.baixinping.framework.common.helper;

import com.baixinping.framework.common.annotation.RequestMapping;
import com.baixinping.framework.common.entity.Handler;
import com.baixinping.framework.common.entity.Request;
import com.sun.org.apache.regexp.internal.RE;
import org.apache.commons.collections4.CollectionUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class ControllerHelper {
    private static final Map<Request, Handler> MAPPING_MAP = new HashMap<Request, Handler>();

    static {
        Set<Class<?>> controllerBean = ClassHelper.getController();
        if (CollectionUtils.isEmpty(controllerBean))
            for (Class<?> clazz : controllerBean){
                Method[] methods = clazz.getDeclaredMethods();
                for (Method method : methods){
                    if (method.isAnnotationPresent(RequestMapping.class)){
                        RequestMapping mapping = method.getAnnotation(RequestMapping.class);
                        String mappingValue = mapping.value();
                        if (mappingValue.matches("\\w+:/\\w*")){
                            String[] split = mappingValue.split(":");
                            if (split.length == 2){
                                Request request = new Request(split[0], split[1]);
                                Handler handler = new Handler(clazz, method);
                                MAPPING_MAP.put(request, handler);
                            }
                        }
                    }
                }


            }
    }

    public static Handler getHandler(String method, String path){
        Request request = new Request(method, path);
        return MAPPING_MAP.get(request);
    }
}
