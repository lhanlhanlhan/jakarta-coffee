package com.jakarta.jakartaback.utils;

import com.alibaba.fastjson.JSONObject;
import com.jakarta.jakartaback.JakartaBackApplication;
import com.jakarta.jakartaback.exceptions.ExceptionType;
import com.jakarta.jakartaback.exceptions.ResultCode;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {

    // 上传文件保存的位置
    public static JSONObject succeededReturn(Object data) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", ResultCode.SUCCESS.code);
        jsonObject.put("data", data);
        return jsonObject;
    }

    public static JSONObject succeededReturn(Object data, String message) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", ResultCode.SUCCESS.code);
        jsonObject.put("data", data);
        jsonObject.put("message", message);
        return jsonObject;
    }

    public static JSONObject succeededReturn(List<Map<String, Object>> data, String message) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", ResultCode.SUCCESS.code);
        jsonObject.put("data", data);
        jsonObject.put("size", data.size());
        jsonObject.put("message", message);
        return jsonObject;
    }

    public static JSONObject succeededReturn(String message) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", ResultCode.SUCCESS.code);
        jsonObject.put("message", message);
        return jsonObject;
    }

    public static JSONObject succeededReturn(String token, String message) {
        HashMap<String, String> tokenMapped = new HashMap<>();
        tokenMapped.put("token", token);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", ResultCode.SUCCESS.code);
        jsonObject.put("message", message);
        jsonObject.put("data", tokenMapped);

        return jsonObject;
    }

    public static JSONObject failedReturn(ExceptionType exception, String message) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", ResultCode.FAILED.code);
        jsonObject.put("exception", exception.code);
        jsonObject.put("message", message);
        return jsonObject;
    }
}
