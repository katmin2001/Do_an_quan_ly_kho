package com.kenzy.manage.do_an_quan_ly_kho.model.request;

import lombok.Data;

import java.util.Date;

@Data
public class ExportReceiptRequest {
    private Long id;
    private String name;
    private Date exportDate;
    private Long orderId;
}
