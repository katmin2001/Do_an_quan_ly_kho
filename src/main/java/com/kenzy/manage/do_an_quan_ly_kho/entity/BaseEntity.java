package com.kenzy.manage.do_an_quan_ly_kho.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@MappedSuperclass
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "status", nullable = true)
    private Boolean status = Boolean.TRUE;
    @Column(name = "created_date", nullable = true)
    private Date createdDate;
    @Column(name = "updated_date", nullable = true)
    private Date updatedDate;
    @Column(name = "created_by", nullable = true)
    private String createdBy;
    @Column(name = "updated_by", nullable = true)
    private Date updatedBy;
}
