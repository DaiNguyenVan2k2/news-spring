package com.ptit.news.exception;

import org.springframework.http.HttpStatus;

public class InvalidRequestException extends BaseException {

    public InvalidRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST, "INVALID_REQUEST");
    }

    public InvalidRequestException(String fieldName, String reason) {
        super(String.format("Invalid %s: %s", fieldName, reason),
                HttpStatus.BAD_REQUEST, "INVALID_REQUEST");
    }
}