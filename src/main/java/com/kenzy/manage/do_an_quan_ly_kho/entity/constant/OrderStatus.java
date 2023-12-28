package com.kenzy.manage.do_an_quan_ly_kho.entity.constant;

public enum OrderStatus {
    IN_PROGRESS(1, "Processing"),
    CANCEL(2, "Cancelled"),
    SHIPPING(3, "Shipping"),
    RETURNED_REFUNDED(4, "Returned/Refunded"),
    COMLPLETED(5, "Completed");

    private final int type;
    private final String name;

    OrderStatus(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
