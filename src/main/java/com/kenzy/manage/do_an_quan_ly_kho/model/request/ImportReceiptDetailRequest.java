package com.kenzy.manage.do_an_quan_ly_kho.model.request;

import lombok.Data;

@Data
public class ImportReceiptDetailRequest {
    private Long productId;
    private long quantity;
}
