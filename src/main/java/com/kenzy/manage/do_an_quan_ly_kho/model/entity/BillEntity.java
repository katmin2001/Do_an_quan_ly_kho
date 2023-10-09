package com.kenzy.manage.do_an_quan_ly_kho.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_bill")
public class BillEntity extends BaseEntity{
    @Column(name = "total_price")
    private BigDecimal totalPrice;
    @Column(name = "order_id")
    private Long orderId;
    @Column(name = "payment_id")
    private Long paymentId;
}