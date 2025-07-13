package com.ptit.news.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.core.JsonParseException;
import com.ptit.news.common.Response;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DeadlockLoserDataAccessException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<Response<String>> handleBaseException(BaseException ex) {
        log.error("BaseException: {}", ex.getMessage(), ex);

        Response<String> response = Response.Error(ex.getMessage());

        return new ResponseEntity<>(response, ex.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response<Map<String, String>>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        log.error("ValidationException: {}", ex.getMessage(), ex);

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        Response<Map<String, String>> response = Response.Error("Validation failed");
        response.setData(errors);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // ========== SECURITY EXCEPTIONS ==========

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Response<String>> handleAccessDeniedException(AccessDeniedException ex) {
        log.error("AccessDeniedException: {}", ex.getMessage(), ex);

        Response<String> response = Response.Error("Access denied. You don't have permission to perform this action.");

        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Response<String>> handleAuthenticationException(AuthenticationException ex) {
        log.error("AuthenticationException: {}", ex.getMessage(), ex);

        Response<String> response = Response.Error("Authentication failed. Please check your credentials.");

        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Response<String>> handleBadCredentialsException(BadCredentialsException ex) {
        log.error("BadCredentialsException: {}", ex.getMessage(), ex);

        Response<String> response = Response.Error("Invalid username or password.");

        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    // ========== JPA/DATABASE EXCEPTIONS ==========

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Response<String>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        log.error("DataIntegrityViolationException: {}", ex.getMessage(), ex);

        String message = "Data integrity violation. ";
        if (ex.getMessage().contains("Duplicate entry")) {
            message += "Record already exists.";
        } else if (ex.getMessage().contains("foreign key constraint")) {
            message += "Referenced record does not exist.";
        } else {
            message += "Please check your data.";
        }

        Response<String> response = Response.Error(message);

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<Response<String>> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex) {
        log.error("EmptyResultDataAccessException: {}", ex.getMessage(), ex);

        Response<String> response = Response.Error("Requested resource not found.");

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(JpaObjectRetrievalFailureException.class)
    public ResponseEntity<Response<String>> handleJpaObjectRetrievalFailureException(
            JpaObjectRetrievalFailureException ex) {
        log.error("JpaObjectRetrievalFailureException: {}", ex.getMessage(), ex);

        Response<String> response = Response.Error("Requested entity not found.");

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    // ========== HTTP REQUEST EXCEPTIONS ==========

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Response<String>> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException ex) {
        log.error("HttpRequestMethodNotSupportedException: {}", ex.getMessage(), ex);

        Response<String> response = Response.Error("HTTP method not supported: " + ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Response<String>> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        log.error("NoHandlerFoundException: {}", ex.getMessage(), ex);

        Response<String> response = Response.Error("Endpoint not found: " + ex.getRequestURL());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Response<String>> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException ex) {
        log.error("MissingServletRequestParameterException: {}", ex.getMessage(), ex);

        Response<String> response = Response.Error("Missing required parameter: " + ex.getParameterName());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Response<String>> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex) {
        log.error("MethodArgumentTypeMismatchException: {}", ex.getMessage(), ex);

        Response<String> response = Response.Error("Invalid parameter type for: " + ex.getName() + ". Expected: "
                + ex.getRequiredType().getSimpleName());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // ========== JSON PARSING EXCEPTIONS ==========

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<Response<String>> handleInvalidFormatException(InvalidFormatException ex) {
        log.error("InvalidFormatException: {}", ex.getMessage(), ex);

        Response<String> response = Response.Error("Invalid JSON format: " + ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JsonParseException.class)
    public ResponseEntity<Response<String>> handleJsonParseException(JsonParseException ex) {
        log.error("JsonParseException: {}", ex.getMessage(), ex);

        Response<String> response = Response.Error("Invalid JSON syntax: " + ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // ========== 5XX SERVER ERRORS ==========

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Response<String>> handleDataAccessException(DataAccessException ex) {
        log.error("DataAccessException: {}", ex.getMessage(), ex);

        Response<String> response = Response.Error("Database access error. Please try again later.");

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DeadlockLoserDataAccessException.class)
    public ResponseEntity<Response<String>> handleDeadlockLoserDataAccessException(
            DeadlockLoserDataAccessException ex) {
        log.error("DeadlockLoserDataAccessException: {}", ex.getMessage(), ex);

        Response<String> response = Response.Error("Database deadlock occurred. Please try again.");

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<Response<String>> handleTransactionSystemException(TransactionSystemException ex) {
        log.error("TransactionSystemException: {}", ex.getMessage(), ex);

        Response<String> response = Response.Error("Transaction failed. Please try again.");

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AsyncRequestTimeoutException.class)
    public ResponseEntity<Response<String>> handleAsyncRequestTimeoutException(AsyncRequestTimeoutException ex) {
        log.error("AsyncRequestTimeoutException: {}", ex.getMessage(), ex);

        Response<String> response = Response.Error("Request timeout. Please try again.");

        return new ResponseEntity<>(response, HttpStatus.REQUEST_TIMEOUT);
    }

    @ExceptionHandler(OutOfMemoryError.class)
    public ResponseEntity<Response<String>> handleOutOfMemoryError(OutOfMemoryError ex) {
        log.error("OutOfMemoryError: {}", ex.getMessage(), ex);

        Response<String> response = Response.Error("Server is experiencing high load. Please try again later.");

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(StackOverflowError.class)
    public ResponseEntity<Response<String>> handleStackOverflowError(StackOverflowError ex) {
        log.error("StackOverflowError: {}", ex.getMessage(), ex);

        Response<String> response = Response.Error("Server error occurred. Please try again later.");

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // ========== GENERIC EXCEPTION ==========

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<String>> handleGenericException(Exception ex) {
        log.error("GenericException: {}", ex.getMessage(), ex);

        Response<String> response = Response.Error("Internal server error. Please try again later.");

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}