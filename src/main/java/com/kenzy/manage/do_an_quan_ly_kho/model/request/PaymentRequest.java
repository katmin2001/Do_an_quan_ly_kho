package com.kenzy.manage.do_an_quan_ly_kho.model.request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class PaymentRequest {
    private Long id;
    private Long orderId;
    private String paymentMethod;
}
