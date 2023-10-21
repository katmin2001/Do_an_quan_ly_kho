package com.kenzy.manage.do_an_quan_ly_kho.model.response;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class ImportReceiptDetailResponse {
    private Long id;
    private String productName;
    private BigDecimal priceUnit;
    private long quantity;
    private BigDecimal totalPriceProduct;
}
