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
@Table(name = "tbl_order")
public class OrderEntity extends BaseEntity{
    @Column(name = "order_date")
    private Date orderDate;
    @Column(name = "order_status")
    private Integer orderStatus;
    @Column(name = "customer_id")
    private Long customerId;
    @Column(name = "total_amount")
    private BigDecimal totalAmount;
    @Column(name = "code")
    private String code;
}
