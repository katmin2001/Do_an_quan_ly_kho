package com.kenzy.manage.do_an_quan_ly_kho.model.request;

import lombok.Data;

@Data
public class ImportReceiptRequest {
    private Long id;
    private String name;
//    private Long wareHouseId;
    private Long productId;
    private long quantity;
}
