package com.kenzy.manage.do_an_quan_ly_kho.model.request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class OrderRequest {
    private Long id;
    private Long customerId;
    private Date orderDate;
    private Integer orderStatus;
    private List<OrderProductRequest> orderProductRequestList;
    private BigDecimal paymentAmount;
    private String paymentMethod;
    private Date paymentDate;
}
