package com.kenzy.manage.do_an_quan_ly_kho.model.response;

import com.kenzy.manage.do_an_quan_ly_kho.entity.BillEntity;
import com.kenzy.manage.do_an_quan_ly_kho.entity.CustomerEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class OrderResponse {
    private Long id;
    private String code;
    private Date orderDate;
    private Integer orderStatus;
    private CustomerEntity customer;
    private List<OrderDetailResponse> orderDetailResponseList;
    private BigDecimal totalAmount;
    private BillEntity bill;
    private String createdBy;
}
