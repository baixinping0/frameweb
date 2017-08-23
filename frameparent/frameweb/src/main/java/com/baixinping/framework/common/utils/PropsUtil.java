package com.baixinping.framework.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropsUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropsUtil.class);
    public static Properties loadPropertiesFile(String fileName) {
        InputStream in = null;
        Properties properties = null;
        try {
            in = PropsUtil.class.getClassLoader().getResourceAsStream(fileName);
            if (in == null)
                throw new FileNotFoundException(fileName+" file is not fonud");
            properties = new Properties();
            properties.load(in);
        } catch (IOException e) {
            LOGGER.error(fileName + " file load failure", e);
        }finally {
            if (in != null)
                try {
                    in.close();
                } catch (IOException e) {
                    LOGGER.error("close inputStream failure", e);
                }
        }
        return properties;
    }

    public static String getString(Properties properties, String key){
        return getString(properties, key, "");
    }
    public static String getString(Properties properties, String key, String def){
        String value = def;
        if (properties.containsKey(key))
            value = properties.getProperty(key);
        return value;
    }

    public static int getInt(Properties properties, String key){
        return getInt(properties, key, 0);
    }

    private static int getInt(Properties properties, String key, int def) {
        int value = def;
        if (properties.containsKey(key))
            value = CastUtil.castInt(properties.getProperty(key));
        return value;
    }

    public static boolean getBoolean(Properties properties, String key){
        return getBoolean(properties, key, false);
    }

    private static boolean getBoolean(Properties properties, String key, boolean def) {
        boolean value = def;
        if (properties.containsKey(key))
            value = CastUtil.caseBoolean(properties.getProperty(key));
        return value;
    }

}
