package com.ptit.news.exception;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class ErrorResponse {

    private String message;
    private String errorCode;
    private int status;
    private Map<String, String> details;
}