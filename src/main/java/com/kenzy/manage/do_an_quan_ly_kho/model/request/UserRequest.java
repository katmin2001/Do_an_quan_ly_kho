package com.kenzy.manage.do_an_quan_ly_kho.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    private String username;
    private String password;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String role;
}
