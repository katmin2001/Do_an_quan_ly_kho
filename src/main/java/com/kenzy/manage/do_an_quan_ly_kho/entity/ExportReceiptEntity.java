package com.kenzy.manage.do_an_quan_ly_kho.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_export_receipt")
public class ExportReceiptEntity extends BaseEntity{
    @Column(name = "export_date")
    private Date exportDate;
    @Column(name = "name")
    private String name;
    @Column(name = "order_id")
    private Long orderId;
    @Column(name = "ware_house_id")
    private Long wareHouseId;
}
