package com.jakarta.jakartaback.controllers;

import com.alibaba.fastjson.JSONObject;
import com.jakarta.jakartaback.exceptions.ExceptionType;
import com.jakarta.jakartaback.login.LoginHandlers;
import com.jakarta.jakartaback.login.LoginInfo;
import com.jakarta.jakartaback.token.PassToken;
import com.jakarta.jakartaback.user.Admin;
import com.jakarta.jakartaback.utils.Databases;
import com.jakarta.jakartaback.utils.Utils;
import com.mysql.cj.jdbc.JdbcConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("test")
public class TestAPIControllers {

    @Autowired
    JdbcTemplate jdbcTemplate;

    // 管理员登录接口
    @CrossOrigin
    @PassToken
    @GetMapping("/select")
    public JSONObject test() {
        System.out.println("Selecting");
        String sql = "SELECT name FROM menu_item";

        // 通过jdbcTemplate查询数据库
        try {
            List<Map<String, Object>> result = Databases.select(sql);

            System.out.println("Name is " + result);
            return Utils.succeededReturn(result);
        } catch (Exception dae) {
            dae.printStackTrace();
            return Utils.failedReturn(ExceptionType.BAD_TOKEN, dae.getMessage());
        }
    }
}
