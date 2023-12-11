package com.kenzy.manage.do_an_quan_ly_kho.model.response;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class BillResponse {
    private Long id;
    private PaymentResponse paymentResponse;
    private Date createdDate;
    private String createdBy;
    private OrderResponse orderResponse;
}
