package com.kenzy.manage.do_an_quan_ly_kho.model.request;

import lombok.Data;

@Data
public class ProductSearchRequest extends SearchRequest{
    private Long categoryId;
    private Long supplierId;
}
