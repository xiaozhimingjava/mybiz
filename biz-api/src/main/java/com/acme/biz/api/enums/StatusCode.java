package com.acme.biz.api.enums;


import org.springframework.http.HttpStatus;

/**
 * 状态码
 * @see HttpStatus
 */
public enum StatusCode {


    OK(0, "OK"),


    FAILED(-1,"Failed"),


    CONTINUE(1,"{status-code.continue}")
    ;


    private final int value;

    private final String  message;


    StatusCode(int value, String message) {
        this.value = value;
        this.message = message;
    }


    public int getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }


    public String getLocalizeMessage() {
        //todo 本地化
        return message;
    }


  


  
  
  
  
  
}

