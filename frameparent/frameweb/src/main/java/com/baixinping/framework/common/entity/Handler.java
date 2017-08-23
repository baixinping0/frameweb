package com.baixinping.framework.common.entity;

import java.lang.reflect.Method;

public class Handler {
    private Class<?> controllerClazz;
    private Method method;

    public Handler(Class<?> controllerClazz, Method method) {
        this.controllerClazz = controllerClazz;
        this.method = method;
    }

    public Class<?> getControllerClazz() {
        return controllerClazz;
    }

    public void setControllerClazz(Class<?> controllerClazz) {
        this.controllerClazz = controllerClazz;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }
}
