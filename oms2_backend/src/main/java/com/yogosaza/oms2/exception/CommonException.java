package com.yogosaza.oms2.exception;

import com.yogosaza.oms2.enums.ErrorCode;

public class CommonException extends RuntimeException {

    private String code;

    public CommonException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public CommonException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
