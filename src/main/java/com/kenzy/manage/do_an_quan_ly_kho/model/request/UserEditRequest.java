package com.kenzy.manage.do_an_quan_ly_kho.model.request;

import lombok.Data;

@Data
public class UserEditRequest {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;
}
