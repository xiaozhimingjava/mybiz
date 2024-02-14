package com.acme.biz.api;

import com.acme.biz.api.enums.StatusCode;
import javafx.util.Builder;

import javax.validation.Valid;

public class ApiResponse<T> {

    public ApiResponse() {
    }

    public ApiResponse(int code, String message, T body) {
        this.code = code;
        this.message = message;
        this.body = body;
    }

    private int code;

    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    @Valid
    private T body;




    public static <T> ApiResponse<T> ok (T body){
        return of(body,StatusCode.OK);
    }

    public static <T> ApiResponse<T> failed(T body,String errorMessage) {
        ApiResponse<T> response = of(body,StatusCode.FAILED);
        response.setMessage(errorMessage);
        return response;
    }

    public static <T> ApiResponse<T> of(T body, StatusCode statusCode) {
        ApiResponse<T> response = new ApiResponse<T>();
        response.setBody(body);
        response.setCode(statusCode.getValue());
        response.setMessage(statusCode.getLocalizeMessage());
        return response;
    }


    public static class Builder<T> {
        int code;

        String message;


        T body;


        public Builder<T> code(int code) {
            this.code = code;
            return this;
        }

        public Builder<T> message(String message) {
            this.message = message;
            return this;
        }

        public Builder<T> body(T body) {
            this.body = body;
            return this;
        }

        public ApiResponse build() {
            return new ApiResponse<>(this.code, this.message, this.body);
        }

    }


}
