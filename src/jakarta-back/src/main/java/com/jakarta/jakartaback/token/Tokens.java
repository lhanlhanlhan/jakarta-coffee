package com.jakarta.jakartaback.token;

import com.auth0.jwt.JWT;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class Tokens {

    // 获取request
    private static HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes) RequestContextHolder
                        .getRequestAttributes();
        return requestAttributes == null ? null : requestAttributes.getRequest();
    }

    // 获取当前token所对的用户名
    public static String getTokenUserId() {
        HttpServletRequest hsr = getRequest();
        if (hsr == null) {
            return null;
        }
        String token = getRequest().getHeader("X-Token");// 从 http 请求头中取出 token
        return JWT.decode(token).getAudience().get(0);
    }
}
