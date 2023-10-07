package com.kenzy.manage.do_an_quan_ly_kho.entity;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImportReceiptEntity extends BaseEntity{
    @Column(name = "import_date")
    private Date importDate;
}
