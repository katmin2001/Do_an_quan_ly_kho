package com.kenzy.manage.do_an_quan_ly_kho.repository;

import com.kenzy.manage.do_an_quan_ly_kho.model.entity.WareHouseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WareHouseRepository extends JpaRepository<WareHouseEntity, Long> {
}
