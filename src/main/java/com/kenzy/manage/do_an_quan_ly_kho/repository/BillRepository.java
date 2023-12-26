package com.kenzy.manage.do_an_quan_ly_kho.repository;

import com.kenzy.manage.do_an_quan_ly_kho.entity.BillEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface BillRepository extends JpaRepository<BillEntity, Long> {
    @Query(value = "SELECT * FROM quan_ly_kho.tbl_bill tb " +
            "WHERE (CAST(:fromDate AS DATETIME) IS NULL OR tb.created_date >= :fromDate) " +
            "AND (CAST(:toDate AS DATETIME) IS NULL OR tb.created_date <= :toDate) ", nativeQuery = true)
    Page<BillEntity> search(Date fromDate, Date toDate, Pageable pageable);
}
