package com.kenzy.manage.do_an_quan_ly_kho.entity.constant;

public enum OrderStatus {
    IN_PROGRESS(1, "Đang xử lý"),
    CANCEL(2, "Hủy"),
    IN_TRANSIT(3, "Đang vận chuyển");

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
