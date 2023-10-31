package com.kenzy.manage.do_an_quan_ly_kho.model.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderDetailResponse {
    private ProductResponse product;
    private int quantity;
    private BigDecimal totalPrice;
}
