package com.kenzy.manage.do_an_quan_ly_kho.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_ware_house")
public class WareHouseEntity extends BaseEntity{
    @Column(name = "ware_house_name")
    private String wareHouseName;
    @Column(name = "location")
    private String location;
    @Column(name = "description")
    private String description;
}
