package com.jakarta.jakartaback.exceptions;

public enum ExceptionType {
    // 权限相关
    INVALID_USER(30001),
    DUPLICATE_USER_NAME(30002),
    WRONG_PASSWORD(30003),
    AUTH_FAILED(30004), // 权限不足

    // tokens相关
    BAD_TOKEN(40001),
    VOID_TOKEN(40002),
    NO_SUCH_USER(40003),
    USER_TYPE_NOT_MATCH(40004),

    // 系统相关
    INVALID_COMING(50001),
    EXECUTE_FAILED(50002);

    public final int code;

    ExceptionType(int c) {
        code = c;
    }
}
