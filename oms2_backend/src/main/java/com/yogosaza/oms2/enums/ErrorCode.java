package com.yogosaza.oms2.enums;


public enum ErrorCode {

    ORDER_NOT_FOUND("C030", "주문이 존재하지 않습니다."),
    ORDER_ALREADY_DELETED("C031", "이미 삭제된 주문입니다."),
    ORDER_NOT_DELETED("C032", "삭제되지 않은 주문 입니다.");


    private String code;
    private String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
