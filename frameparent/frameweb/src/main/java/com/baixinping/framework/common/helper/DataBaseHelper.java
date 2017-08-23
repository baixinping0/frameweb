package com.baixinping.framework.common.helper;

import com.baixinping.framework.common.utils.PropsUtil;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public final class DataBaseHelper {
    private static final QueryRunner QUERY_RUNNER = new QueryRunner();
    private static final ThreadLocal<Connection> CONNECTION_HOLDER = new ThreadLocal<Connection>();
    private static final BasicDataSource DATA_SOURCE = new BasicDataSource();

    private static final Logger LOGGER = LoggerFactory.getLogger(DataBaseHelper.class);

    private static final String  DRIVER_KEY = "jdbc.driverClassName";
    private static final String URL_KEY = "jdbc.url";
    private static final String USERNAME_KEY = "jdbc.username";
    private static final String PASSWORD_KEY = "jdbc.password";

    private static final String  DRIVER;
    private static final String URL;
    private static final String USERNAME;
    private static final String PASSWORD;
    static {
        Properties properties = PropsUtil.loadPropertiesFile("config.properties");
        DRIVER = PropsUtil.getString(properties, DRIVER_KEY);
        URL = PropsUtil.getString(properties, URL_KEY);
        USERNAME = PropsUtil.getString(properties, USERNAME_KEY);
        PASSWORD = PropsUtil.getString(properties, PASSWORD_KEY);

        DATA_SOURCE.setDriverClassName(DRIVER);
        DATA_SOURCE.setUrl(URL);
        DATA_SOURCE.setUsername(USERNAME);
        DATA_SOURCE.setPassword(PASSWORD);
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            LOGGER.error("not load jdbc driver", e);
        }
    }

    public static void executeSqlFile(String fileName){
        InputStream in = DataBaseHelper.class.getClassLoader().getResourceAsStream(fileName);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line = null;
        try {
            while ((line = reader.readLine()) != null){
                DataBaseHelper.changeDate(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection(){
        Connection conn = null;
        if (conn == null)
            try {
                conn = DATA_SOURCE.getConnection();
            } catch (SQLException e) {
                LOGGER.error("get connection failure", e);
            }finally {
                CONNECTION_HOLDER.set(conn);
            }
        return  conn;
    }

    public static void closeConnection(){
//        Connection conn = CONNECTION_HOLDER.get();
//        if (conn != null)
//            try {
//                conn.close();
//            } catch (SQLException e) {
//                LOGGER.error("close connection failure", e);
//            }finally {
                CONNECTION_HOLDER.remove();
//            }
    }


    public static <T> List<T> select(Class<T> tClass){
        String sql = "select * from customer";
        List<T> list = null;
        try {
            Connection conn = getConnection();
            list = QUERY_RUNNER.query(conn, sql, new BeanListHandler<T>(tClass));
        } catch (SQLException e) {
            LOGGER.error("query entity list failure", e);
        }finally {
            closeConnection();
        }
        return list;
    }
    public static <T> T selectOne(Serializable id, Class<T> tClass){
        String sql = "select * from customer where id = " + id;
        T entity = null;
        Connection conn = getConnection();
        try {
            entity = QUERY_RUNNER.query(conn, sql, new BeanHandler<T>(tClass));
        } catch (SQLException e) {
            LOGGER.error("query entity failure");
        }finally {
            closeConnection();
        }
        return entity;
    }

    public static  List<Map<String, Object>> selectMap(String sql, Object...params){
        List<Map<String, Object>> list = null;
        Connection conn = getConnection();

        try {
            list = QUERY_RUNNER.query(conn, sql, new MapListHandler(), params);
        } catch (SQLException e) {
            LOGGER.error("selectMap failure", e);
        }finally {
            closeConnection();
        }
        return list;
    }

    /**
     * 此方法可以执行insert， delete， update语句
     * @param sql
     * @param params
     * @param <T>
     * @return
     */
    public static <T> int changeDate(String sql, Object...params){
        Connection conn = getConnection();
        int row = 0;
        try {
            row = QUERY_RUNNER.update(conn, sql, params);
        } catch (SQLException e) {
            LOGGER.error("execute update failure" + e);
        }
        return row;
    }

    public static <T> boolean insert(T t){
        StringBuilder sql = new StringBuilder();
        sql.append("insert into " + t.getClass().getSimpleName().toLowerCase());
        StringBuilder values = new StringBuilder();
        sql.append(" ( ");
        values.append(" values(");

        for(Field field : t.getClass().getDeclaredFields()){
            field.setAccessible(true);
            sql.append(field.getName() + ",");
            try {
                values.append("'"+field.get(t) + "',");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        sql.replace(sql.lastIndexOf(","), sql.length(),")");
        values.replace(values.lastIndexOf(","), values.length(), ")");
        sql.append(values);
        System.out.println("sql  " + sql.toString());
        return changeDate(sql.toString(), null) == 1;
    }

    public static<T> boolean update(T t){
        StringBuilder sql = new StringBuilder();
        sql.append("update " + t.getClass().getSimpleName().toLowerCase() + " set ");
        for(Field field : t.getClass().getDeclaredFields()){
            field.setAccessible(true);
            try {
                if (field.get(t) != null)
                    sql.append(field.getName()+"='"+field.get(t) + "' , ");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        sql.delete(sql.lastIndexOf(","), sql.length()-1);

        sql.append(" where id = '" + getId(t) +"'");
        return changeDate(sql.toString(), null) == 1;
    }

    public static<T> boolean delete(Serializable id){
        String sql = "delete from customer where id='"+id+"'";
        return changeDate(sql, null) == 1;
    }

    private static <T> Object getId(T t){
        Field field = null;
        Object id = null;
        try {
            field = t.getClass().getDeclaredField("id");
            field.setAccessible(true);
            id = field.get(t);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

}
