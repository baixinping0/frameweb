package com.baixinping.framework.common.proxy;

import java.lang.reflect.Method;

public class AspectProxy implements Proxy {
    public Object doProxy(ProxyChain proxyChain) {
        Object result = null;
        Class<?> targetClazz = proxyChain.getTargetClass();
        Method targetMethod = proxyChain.getTargetMethod();
        Object[] params = proxyChain.getMethodParams();
        begin();
        try{
            if (intersept(targetClazz, targetMethod, params)){
                before(targetClazz, targetMethod, params);
                result = proxyChain.doProxyChain();
                after(targetClazz, targetMethod, params, result);
            }else {
                result = proxyChain.doProxyChain();
            }
        }catch (Throwable throwable) {
            error(targetClazz, targetMethod, params, throwable);
            throwable.printStackTrace();
        } finally {
            end(targetClazz, targetMethod, params);
        }
        return result;
    }

    public void begin(){ }

    public boolean intersept(Class<?> clazz, Method method, Object[] params){
        return true;
    }
    public void before(Class<?> clazz, Method method, Object[] params){}

    public void after(Class<?> clazz, Method method, Object[] params, Object result){}

    public void error(Class<?> clazz, Method method, Object[] params, Throwable throwable){}

    public void end(Class<?> clazz, Method method, Object[] params){}
}
