package com.kenzy.manage.do_an_quan_ly_kho.model.request;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class ProductRequest {
    private Long id;
    private String productName;
    private String description;
    private BigDecimal price;
    private String[] productImages;
    private Long supplierId;
    private Long categoryId;
}
