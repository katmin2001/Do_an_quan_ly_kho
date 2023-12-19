package com.kenzy.manage.do_an_quan_ly_kho.entity;

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
@Table(name = "tbl_export_receipt_detail")
public class ExportReceiptDetailEntity extends BaseEntity{
    @Column(name = "export_receipt_id")
    private Long exportReceiptId;
    @Column(name = "product_id")
    private Long productId;
    @Column(name = "quantity")
    private int quantity;
    @Column(name = "total_price")
    private BigDecimal totalPrice;
    @Column(name = "unit_price")
    private BigDecimal unitPrice;
    @Column(name = "import_receipt_id")
    private Long importReceiptId;
}
