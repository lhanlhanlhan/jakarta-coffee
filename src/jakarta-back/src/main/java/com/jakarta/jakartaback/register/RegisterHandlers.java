package com.jakarta.jakartaback.register;

import com.alibaba.fastjson.JSONObject;
import com.jakarta.jakartaback.exceptions.ExceptionType;
import com.jakarta.jakartaback.user.Client;
import com.jakarta.jakartaback.user.UserHandlers;
import com.jakarta.jakartaback.utils.Utils;

public class RegisterHandlers {

    // TODO - 提交客户注册信息 checked
    public static JSONObject submitRegisterInfo(RegisterInfo ri) {
        ri.setAdmin(false);
        if (Client.getUserByUsername(ri.getUsername()) != null) {
            return Utils.failedReturn(ExceptionType.DUPLICATE_USER_NAME, "The username has been occupied. Please try another one.");
        } else return UserHandlers.addUser(ri);
    }
}
