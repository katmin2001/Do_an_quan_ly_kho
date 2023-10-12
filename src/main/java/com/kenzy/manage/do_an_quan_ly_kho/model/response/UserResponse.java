package com.kenzy.manage.do_an_quan_ly_kho.model.response;

import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String username;
    private String name;
    private String email;
    private String address;
    private String phone;

}
