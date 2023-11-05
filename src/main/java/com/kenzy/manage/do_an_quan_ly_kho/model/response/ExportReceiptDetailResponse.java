package com.kenzy.manage.do_an_quan_ly_kho.model.response;

import com.kenzy.manage.do_an_quan_ly_kho.entity.OrderEntity;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExportReceiptDetailResponse {
    private Long id;
    private ProductResponse productResponse;
    private int quantity;
    private BigDecimal totalPrice;
    private BigDecimal unitPrice;
    private OrderEntity order;
}
