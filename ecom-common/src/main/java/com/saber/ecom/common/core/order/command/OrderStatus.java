package com.saber.ecom.common.core.order.command;

public enum OrderStatus {
    PAID(1),CANCELLED(5),SHIPPED(2),
    DELIVERED(3),DELIVERY_FAILED(4);

    private final int value;

    OrderStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
