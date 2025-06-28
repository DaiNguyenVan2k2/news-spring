package com.ptit.news.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BaseException extends RuntimeException {

    private final HttpStatus status;
    private final String errorCode;

    public BaseException(String message) {
        super(message);
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
        this.errorCode = "INTERNAL_ERROR";
    }

    public BaseException(String message, HttpStatus status) {
        super(message);
        this.status = status;
        this.errorCode = status.name();
    }

    public BaseException(String message, HttpStatus status, String errorCode) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
    }
}