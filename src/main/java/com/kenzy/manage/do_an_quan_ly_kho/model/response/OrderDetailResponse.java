package com.kenzy.manage.do_an_quan_ly_kho.model.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderDetailResponse {
    private Long id;
    private Long productId;
    private String productName;
    private BigDecimal priceUnit;
    private int quantity;
    private BigDecimal totalPrice;
    private List<String> imageUrls;
}
