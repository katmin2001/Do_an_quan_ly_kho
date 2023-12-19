package com.kenzy.manage.do_an_quan_ly_kho.model.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ImportReceiptDetailResponse {
    private Long id;
    private String productName;
    private BigDecimal priceUnit;
    private long quantity;
    private BigDecimal totalPriceProduct;
    private List<String> imageUrls;
}
