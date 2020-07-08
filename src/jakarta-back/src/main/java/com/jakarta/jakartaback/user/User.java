package com.jakarta.jakartaback.user;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;

public interface User {

    // 获取用户密码
    String getPassword();

    // 获取用户名字
    String getUsername();

    // 获取用户Email
    String getEmail();

    // 获取用户Tel
    String getTelephone();

    // 获取用户是否是admin
    boolean isAdmin();

    // 获取Token
    String getToken();

    // 访问数据库，返回该用户是否有效
    boolean isNotValid();

    // 访问数据库，返回该用户名是否存在超过1个
    boolean hasDuplicateUsername();

    // 根据Token获取用户名
    static String getUsernameByToken(String token) {
        // 验证是否是"客户"
        Claim position;
        try {
            position = JWT.decode(token).getClaim("position");
        } catch (JWTDecodeException j) {
            return null;
        }
        if (!position.asString().equals("client")) {
            return null;
        }
        // 获取 token 中的 username
        String username;
        try {
            username = JWT.decode(token).getAudience().get(0);
        } catch (JWTDecodeException j) {
            return null;
        }
        return username;
    }
}
