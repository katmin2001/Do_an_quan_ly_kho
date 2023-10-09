package com.kenzy.manage.do_an_quan_ly_kho.model.entity.constant;

import com.kenzy.manage.do_an_quan_ly_kho.model.entity.CategoryEntity;
import lombok.Data;

@Data
public class Result {
    private String message;
    private String status;
    private Object data;

    public Result(String message, String status, Object data) {
        this.message = message;
        this.status = status;
        this.data = data;
    }
}
