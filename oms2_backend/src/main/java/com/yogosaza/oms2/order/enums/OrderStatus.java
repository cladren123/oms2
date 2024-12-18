package com.yogosaza.oms2.order.enums;

public enum OrderStatus {

    PAYMENT_COMPLETE("결제 완료"),
    SHIPPING("배송 중"),
    DELIVERED("배송 완료"),
    RETURNED("반품"),
    EXCHANGED("교환");

    private String status;

    OrderStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
