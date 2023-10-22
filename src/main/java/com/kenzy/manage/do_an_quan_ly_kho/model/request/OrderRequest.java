package com.kenzy.manage.do_an_quan_ly_kho.model.request;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class OrderRequest {
    private Long id;
    private Long customerId;
    private Date orderDate;
    private String orderStatus;
    private ExportReceiptRequest exportReceiptRequest;
    private List<OrderProductRequest> orderProductRequestList;
}
