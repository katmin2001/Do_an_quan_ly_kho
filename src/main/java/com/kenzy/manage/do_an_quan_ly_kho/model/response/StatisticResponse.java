package com.kenzy.manage.do_an_quan_ly_kho.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class StatisticResponse {
    private BigDecimal revenue;
    private BigDecimal costs;
    private BigDecimal profit;
}
