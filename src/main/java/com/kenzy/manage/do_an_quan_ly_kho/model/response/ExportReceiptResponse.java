package com.kenzy.manage.do_an_quan_ly_kho.model.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ExportReceiptResponse {
    private Long id;
    private Date exportDate;
    private Date createdDate;
    private String createdBy;
    private String name;
    private Boolean status;
    private BigDecimal totalPrice;
}
