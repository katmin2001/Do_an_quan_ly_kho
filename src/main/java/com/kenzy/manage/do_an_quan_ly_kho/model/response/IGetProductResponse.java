package com.kenzy.manage.do_an_quan_ly_kho.model.response;

import java.math.BigDecimal;
import java.util.List;

public interface IGetProductResponse {
    Long getId();
    String getProductName();
    String getDescription();
    BigDecimal getPrice();
    String getProductImages();
    String getContactName();
    String getName();
}
