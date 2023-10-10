package com.kenzy.manage.do_an_quan_ly_kho.model.request;

import lombok.Data;

@Data
public class SupplierRequest {
    private Long id;
    private String contactName;
    private String email;
    private String phone;
    private String address;
}
