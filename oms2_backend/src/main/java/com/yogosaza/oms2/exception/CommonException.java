package com.yogosaza.oms2.exception;

public class CommonException extends Exception{

    private String code;

    public CommonException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
