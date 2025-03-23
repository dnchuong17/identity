package com.example.identityservice.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Getter;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    USERNAME_INVALID(1001, "Username must be have {min} characters", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1002, "Password must be have {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_KEY(1003, "Invalid message key", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1004, "User is not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1005, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    USER_EXISTED(1000, "User existed", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(1006, "Do not have permission", HttpStatus.FORBIDDEN),
    INVALID_DOB(1007, "Your age must be at least {min}", HttpStatus.BAD_REQUEST);

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private int code;
    private String message;
    private HttpStatusCode statusCode;
}
