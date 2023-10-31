package com.kenzy.manage.do_an_quan_ly_kho.model.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderPaymentResponse {
    private Long id;
    private Date orderDate;
    private String orderStatus;
    private String nameCustomer;
}
