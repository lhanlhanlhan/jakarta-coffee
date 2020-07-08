package com.jakarta.jakartaback.exceptions;

public enum ResultCode {
    // 成功
    SUCCESS(200),
    // 失败
    FAILED(400),
    // 未认证
    UNAUTHORIZED(401),
    // 接口不存在
    NOT_FOUND(404),
    // 内部错误
    INTERNAL_SERVER_ERROR(500);

    public final int code;

    ResultCode(int code) {
        this.code = code;
    }
}
