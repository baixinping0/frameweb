package com.baixinping.framework.common.helper;

import com.baixinping.framework.common.annotation.Aspect;
import com.baixinping.framework.common.proxy.AspectProxy;
import com.baixinping.framework.common.proxy.Proxy;
import com.baixinping.framework.common.proxy.ProxyManager;
import com.baixinping.framework.common.utils.ReflectUtil;

import java.lang.annotation.Annotation;
import java.util.*;

public class AopHelper {

    static {
        Map<Class<?>, List<Proxy>> targetMap = createTargetMap();
        for (Map.Entry<Class<?>, List<Proxy>> entry : targetMap.entrySet()){
            Object proxy = ProxyManager.createProxy(entry.getKey(), entry.getValue());
            BeanHelper.setBean(entry.getKey(), proxy);
        }
    }
    /**
     * get all class by aspect annotation
     * @param aspect
     * @return
     */
    private static Set<Class<?>> createTargetClassSet(Aspect aspect){
        Set<Class<?>> targetClassSet = new HashSet<Class<?>>();
        Class<? extends Annotation> annotation = aspect.value();
        if (annotation != null &&  !annotation.equals(Aspect.class)){
            targetClassSet.addAll(ClassHelper.getClassSetByAnnotation(annotation));
        }
        return targetClassSet;
    }

    /**
     *
     * @return Map<AspectImpl, Set<Target>>
     */
    private static Map<Class<?>, Set<Class<?>>> createProxyMap(){
        Map<Class<?>, Set<Class<?>>> proxyMap = new HashMap<Class<?>, Set<Class<?>>>();
        Set<Class<?>> aspectSetClass = ClassHelper.getClassSetBySuper(AspectProxy.class);
        for (Class<?> aspect : aspectSetClass){
            if (aspect.isAnnotationPresent(Aspect.class)){
                Aspect aspectAnnotation = aspect.getAnnotation(Aspect.class);
                Set<Class<?>> targetClassSet = createTargetClassSet(aspectAnnotation);
                proxyMap.put(aspect, targetClassSet);
            }
        }
        return proxyMap;
     }

    /**
     *
     * @return Map<target, List<ProxyImpl>>
     */
     private static Map<Class<?>, List<Proxy>> createTargetMap(){
         Map<Class<?>, Set<Class<?>>> proxyMap = createProxyMap();
         Map<Class<?>, List<Proxy>> targetMap = new HashMap<Class<?>, List<Proxy>>();

         for (Map.Entry<Class<?>, Set<Class<?>>> entry : proxyMap.entrySet()){
             Class<?> aspectImpl = entry.getKey();
             Proxy aspectInstance = (Proxy) ReflectUtil.newInstance(aspectImpl);

             Set<Class<?>> targetSet = entry.getValue();
             for (Class<?> target : targetSet){
                if (targetMap.containsKey(target)){
                    targetMap.get(target).add(aspectInstance);
                }else {
                    List<Proxy> list = new ArrayList<Proxy>();
                    list.add(aspectInstance);
                    targetMap.put(target, new ArrayList<Proxy>(list));
                }
             }

         }
         return targetMap;
     }

}
