package com.jakarta.jakartaback.exceptions;

public class JakartaTokenException extends Exception {
    private final ExceptionType exceptionType;
    private final String description;

    public JakartaTokenException(ExceptionType et) {
        this.exceptionType = et;
        switch (et) {
            case BAD_TOKEN:
                description = "Not a valid token. Please login again.";
                break;
            case NO_SUCH_USER:
                description = "User does not exist. Please login again";
                break;
            case VOID_TOKEN:
                description = "The token was void. Permission denied.";
                break;
            default:
                description = "Unknown exception.";
                break;
        }
    }

    public int getExceptionCode() {
        return exceptionType.code;
    }

    public String getDescription() {
        return description;
    }
}
