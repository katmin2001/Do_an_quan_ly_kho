package com.kenzy.manage.do_an_quan_ly_kho.model.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BillRequest {
    private Long id;
    private Long paymentId;
}
