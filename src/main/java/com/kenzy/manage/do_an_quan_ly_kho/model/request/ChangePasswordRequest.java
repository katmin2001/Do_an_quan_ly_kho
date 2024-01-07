package com.kenzy.manage.do_an_quan_ly_kho.model.request;

import lombok.Data;

@Data
public class ChangePasswordRequest {
    private String username;
    private String password;
    private String rePassword;
}
