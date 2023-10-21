package com.kenzy.manage.do_an_quan_ly_kho.model.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
@Data
public class ImportReceiptResponse {
    private Long id;
    private String name;
    private Date importDate;
    private List<ImportReceiptDetailResponse> importReceiptDetailResponseList;
    private BigDecimal totalPrice;
}
