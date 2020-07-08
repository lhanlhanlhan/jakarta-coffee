package com.jakarta.jakartaback;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.jakarta.jakartaback.exceptions.ExceptionType;
import com.jakarta.jakartaback.exceptions.JakartaTokenException;
import com.jakarta.jakartaback.token.AdminLoginToken;
import com.jakarta.jakartaback.token.ClientLoginToken;
import com.jakarta.jakartaback.token.PassToken;
import com.jakarta.jakartaback.user.Admin;
import com.jakarta.jakartaback.user.Client;
import com.jakarta.jakartaback.user.User;
import com.jakarta.jakartaback.utils.Utils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class AuthenticationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {
        // 仅供测试
        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes) RequestContextHolder
                        .getRequestAttributes();
        assert requestAttributes != null;
        System.out.println(requestAttributes.getRequest().getRequestURI());

        // 从 http 请求头中取出 token
        String token = httpServletRequest.getHeader("X-Token");
        // 如果不是映射到方法直接通过
        if(!(handler instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod)handler;
        Method method = handlerMethod.getMethod();
        // 检查是否有PassToken注释，有则跳过认证
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;
            }
        }
        if (token == null) {
            throw new JakartaTokenException(ExceptionType.VOID_TOKEN);
        }
        // 检查有没有需要【用户】权限的注解，有，就需要用户鉴定
        if (method.isAnnotationPresent(ClientLoginToken.class)) {
            ClientLoginToken clientLoginToken = method.getAnnotation(ClientLoginToken.class);
            if (clientLoginToken.required()) {
                // 验证是否是"客户"
                Claim position;
                try {
                    position = JWT.decode(token).getClaim("position");
                } catch (JWTDecodeException j) {
                    throw new JakartaTokenException(ExceptionType.BAD_TOKEN);
                }
                if (!position.asString().equals("client")) {
                    throw new JakartaTokenException(ExceptionType.USER_TYPE_NOT_MATCH);
                }
                // 获取 token 中的 username
                String username;
                try {
                    username = JWT.decode(token).getAudience().get(0);
                } catch (JWTDecodeException j) {
                    throw new JakartaTokenException(ExceptionType.BAD_TOKEN);
                }
                // 验证user
                Client theUser = Client.getUserByUsername(username);
                checkTokenPassword(token, theUser);
                // 其他情况，允许登录！
                return true;
            }
        }
        // 检查是否需要【管理员】身份的注解，有，就需要鉴定
        else if (method.isAnnotationPresent(AdminLoginToken.class)) {
            AdminLoginToken adminLoginToken = method.getAnnotation(AdminLoginToken.class);
            if (adminLoginToken.required()) {
                // 验证是否是"管理员"
                Claim position;
                try {
                    position = JWT.decode(token).getClaim("position");
                } catch (JWTDecodeException j) {
                    throw new JakartaTokenException(ExceptionType.BAD_TOKEN);
                }
                if (!position.asString().equals("admin")) {
                    throw new JakartaTokenException(ExceptionType.USER_TYPE_NOT_MATCH);
                }
                // 获取 token 中的 username
                String username;
                try {
                    username = JWT.decode(token).getAudience().get(0);
                } catch (JWTDecodeException j) {
                    throw new JakartaTokenException(ExceptionType.BAD_TOKEN);
                }
                // 验证user
                Admin theUser = Admin.getUserByUsername(username);
                checkTokenPassword(token, theUser);
                System.out.println(5);
                // 其他情况，允许登录！
                return true;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

    // 检测一个token和对应的user类的密码是否一致
    private void checkTokenPassword(String token, User theUser) throws JakartaTokenException {
        if (theUser == null) {
            throw new JakartaTokenException(ExceptionType.NO_SUCH_USER);
        }
        // 验证 token
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(theUser.getPassword())).build();
        try {
            jwtVerifier.verify(token);
        } catch (JWTVerificationException e) {
            throw new JakartaTokenException(ExceptionType.BAD_TOKEN);
        }
    }
}