package com.example.identityservice.exeption;

import lombok.Getter;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    USERNAME_INVALID(1001, "Username must be have 12 characters", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1002, "Password must be have 8 characters", HttpStatus.BAD_REQUEST),
    INVALID_KEY(1003, "Invalid message key", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1004, "User is not existed", HttpStatus.NOT_FOUND),
    UNAUTHORIZED(1005, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    USER_EXISTED(1000, "User existed", HttpStatus.BAD_REQUEST);

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private int code;
    private String message;
    private HttpStatusCode statusCode;
}
