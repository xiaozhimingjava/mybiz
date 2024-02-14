package com.acme.biz.web.mvc.exception;


import com.acme.biz.api.ApiResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ValidationException;

@RestControllerAdvice
public class ExceptionHandlerConfiguration {


    @ExceptionHandler(ValidationException.class)
    public ApiResponse<Void> onValidationException(ValidationException e) {
        return ApiResponse.failed(null, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<Void> onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ApiResponse.failed(null, e.getMessage());
    }
}
