package com.ptit.news.common;

import com.ptit.news.common.enums.StatusResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Response<T> {

    public T data;
    public String message;
    public String errorMessage;
    public StatusResponse status; // Success // Fail

    public static <T> Response<T> Success(T data, String message) {
        Response<T> response = new Response<>();
        response.data = data;
        response.message = message;
        response.status = StatusResponse.Success;
        return response;
    }

    // Static factory method for error response
    public static <T> Response<T> Error(String errorMessage) {
        Response<T> response = new Response<>();
        response.errorMessage = errorMessage;
        response.status = StatusResponse.Fail;
        return response;
    }

}
