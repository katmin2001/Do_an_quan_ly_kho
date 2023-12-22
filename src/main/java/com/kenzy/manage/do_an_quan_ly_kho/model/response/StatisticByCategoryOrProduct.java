package com.kenzy.manage.do_an_quan_ly_kho.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class StatisticByCategoryOrProduct {
    private String name;
    private BigDecimal revenue;
    private BigDecimal costs;
    private BigDecimal profit;
}
