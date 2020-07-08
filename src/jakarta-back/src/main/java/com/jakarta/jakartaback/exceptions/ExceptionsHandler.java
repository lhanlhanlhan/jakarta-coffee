package com.jakarta.jakartaback.exceptions;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ExceptionsHandler {

    @ResponseBody
    @ExceptionHandler(JakartaTokenException.class)
    public Object handleException(JakartaTokenException e) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", ResultCode.UNAUTHORIZED.code);
        jsonObject.put("exception_code", e.getExceptionCode());
        jsonObject.put("message", e.getDescription());
        return jsonObject;
    }

    // 普通接口错误
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Object handleException(Exception e) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", ResultCode.INTERNAL_SERVER_ERROR.code);
        jsonObject.put("message", "Internal server error.");
        return jsonObject;
    }
}
