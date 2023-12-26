package com.kenzy.manage.do_an_quan_ly_kho.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_bill")
public class BillEntity extends BaseEntity{
    @Column(name = "total_price")
    private BigDecimal totalPrice;
    @Column(name = "payment_amount")
    private BigDecimal paymentAmount;
    @Column(name = "payment_method")
    private String paymentMethod;
    @Column(name = "payment_date")
    private Date paymentDate;
    @Column(name = "order_id")
    private Long orderId;
}
