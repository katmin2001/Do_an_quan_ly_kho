package com.kenzy.manage.do_an_quan_ly_kho.model.request;

import lombok.Data;

@Data
public class OrderProductRequest {
    private Long productId;
    private int quantity;
}
