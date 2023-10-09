package com.kenzy.manage.do_an_quan_ly_kho.model.entity;

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
@Table(name = "tbl_import_receipt")
public class ImportReceiptEntity extends BaseEntity{
    @Column(name = "import_date")
    private Date importDate;
}