package com.kenzy.manage.do_an_quan_ly_kho.model.response;

import com.kenzy.manage.do_an_quan_ly_kho.entity.CustomerEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class OrderResponse {
    private Long id;
    private Date orderDate;
    private String orderStatus;
    private CustomerEntity customer;
    private List<OrderDetailResponse> orderDetailResponseList;
    private BigDecimal totalAmount;
}
