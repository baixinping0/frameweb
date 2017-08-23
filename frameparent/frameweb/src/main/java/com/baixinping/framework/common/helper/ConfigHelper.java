package com.baixinping.framework.common.helper;

import com.baixinping.framework.common.config.ConfigConstant;
import com.baixinping.framework.common.utils.PropsUtil;

import java.util.Properties;

public class ConfigHelper {
    private static final Properties CONFIG = PropsUtil.loadPropertiesFile(ConfigConstant.CONFIG_FILE);

    public static String getJdbcDriver(){
        return PropsUtil.getString(CONFIG, ConfigConstant.JDBC_DRIVER);
    }
    public static String getJdbcUrl(){
        return PropsUtil.getString(CONFIG, ConfigConstant.JDBC_URL);
    }
    public static String getJdbcUserName(){
        return PropsUtil.getString(CONFIG, ConfigConstant.JDBC_USERNAME);
    }
    public static String getJdbcPassword(){
        return PropsUtil.getString(CONFIG, ConfigConstant.JDBC_PASSWORD);
    }

    public static String getAppBasePackage(){
        return PropsUtil.getString(CONFIG, ConfigConstant.APP_BASE_PACKAGE);
    }
    public static String getAppJspPath(){
        return PropsUtil.getString(CONFIG, ConfigConstant.APP_JSP_PATH);
    }
    public static String getAppStaticPath(){
        return PropsUtil.getString(CONFIG, ConfigConstant.APP_STATIC_PATH);
    }
}
