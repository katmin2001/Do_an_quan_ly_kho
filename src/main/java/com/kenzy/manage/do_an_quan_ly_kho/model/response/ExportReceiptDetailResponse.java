package com.kenzy.manage.do_an_quan_ly_kho.model.response;

import com.kenzy.manage.do_an_quan_ly_kho.entity.OrderEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ExportReceiptDetailResponse {
    private Long id;
    private String productName;
    private long quantity;
    private BigDecimal totalPriceProduct;
    private BigDecimal unitPrice;
    private List<String> imageUrls;
//    private
}
