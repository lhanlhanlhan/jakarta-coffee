package com.jakarta.jakartaback.utils;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

public class Databases {

    private static JdbcTemplate dbInstance;

    // 执行数据库Select操作
    public static List<Map<String, Object>> select(String sql) {
        try {
            return dbInstance.queryForList(sql);
        } catch (DataAccessException e) {
            System.err.println("Database Error: " + e.getMessage());
            return null;
        } catch (NullPointerException e) {
            System.err.println("The " + (sql == null ? "SQL sentence" : "database") + " was nil in select");
            return null;
        }
    }

    // 执行数据库其他操作
    public static boolean execute(String sql) {
        try {
            dbInstance.execute(sql);
            return true;
        } catch (DataAccessException e) {
            System.err.println("Database Error: " + e.getMessage());
            return false;
        } catch (NullPointerException e) {
            System.err.println("The " + (sql == null ? "SQL sentence" : "database") + " was nil.");
            return false;
        }
    }

    // 设置数据库对象（限制使用）
    public static void setDB(JdbcTemplate db) {
        System.out.println("The JDBCTemplate was set");
        if (db == null) {
            throw new NullPointerException("the JDBCTemplate was null.");
        }
        dbInstance = db;
    }
}
