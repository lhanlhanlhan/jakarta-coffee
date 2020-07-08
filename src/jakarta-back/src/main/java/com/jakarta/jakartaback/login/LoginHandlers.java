package com.jakarta.jakartaback.login;

import com.alibaba.fastjson.JSONObject;
import com.jakarta.jakartaback.exceptions.ExceptionType;
import com.jakarta.jakartaback.user.User;
import com.jakarta.jakartaback.utils.Utils;

public class LoginHandlers {

    // 检查用户是否有效
    public static JSONObject checkLoginInfo(User theUser) {
        if (theUser.isNotValid()) {
            return Utils.failedReturn(ExceptionType.INVALID_USER, "Login failed because wrong username or password.");
        } else {
            return Utils.succeededReturn(theUser.getToken(), "Login succeeded.");
        }
    }
}
