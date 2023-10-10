package com.kenzy.manage.do_an_quan_ly_kho.repository;

import com.kenzy.manage.do_an_quan_ly_kho.entity.WareHouseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface WareHouseRepository extends JpaRepository<WareHouseEntity, Long> {
    @Query(value = "SELECT * FROM quan_ly_kho.tbl_ware_house tw " +
            "WHERE (CAST(:fromDate AS DATETIME) IS NULL OR tw.created_date >= :fromDate) " +
            "AND (CAST(:toDate AS DATETIME) IS NULL OR tw.created_date <= :toDate) " +
            "AND (LOWER(tw.ware_house_name) like CONCAT('%', LOWER(:keyword), '%') or tw.description like CONCAT('%', LOWER(:keyword), '%')) AND tw.status = true ",
            nativeQuery = true)
    Page<WareHouseEntity> search(String keyword, Date fromDate, Date toDate, Pageable pageable);
}
