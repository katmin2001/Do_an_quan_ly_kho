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
@Table(name = "tbl_product")
public class ProductEntity extends BaseEntity{
    @Column(name = "product_name")
    private String productName;
    @Column(name = "description")
    private String description;
    @Column(name = "price")
    private BigDecimal price;
    @Column(name = "product_image")
    private String[] productImages;
    @Column(name = "supplier_id")
    private Long supplierId;
    @Column(name = "category_id")
    private Long categoryId;
}
