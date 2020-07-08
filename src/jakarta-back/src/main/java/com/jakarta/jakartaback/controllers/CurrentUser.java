package com.jakarta.jakartaback.controllers;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CurrentUser {
    // 这是一个使得get方法体能获取到token中用户信息的注解（参数级注解）
}