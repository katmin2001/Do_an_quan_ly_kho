package com.kenzy.manage.do_an_quan_ly_kho.model.request;

import lombok.Data;

@Data
public class WareHouseRequest {
    private Long id;
    private String name;
    private String location;
    private String description;
}
