package com.kenzy.manage.do_an_quan_ly_kho.model.response;

import java.math.BigDecimal;

public interface IGetStatisticByCategoryOrProduct {
    String getName();
    BigDecimal getRevenue();
    BigDecimal getCosts();
}
