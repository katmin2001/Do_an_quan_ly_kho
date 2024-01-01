package com.kenzy.manage.do_an_quan_ly_kho.repository;

import com.kenzy.manage.do_an_quan_ly_kho.entity.ExportReceiptEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface ExportReceiptRepository extends JpaRepository<ExportReceiptEntity, Long> {
    @Query(value = "SELECT * " +
            "FROM quan_ly_kho.tbl_export_receipt er " +
            "WHERE (CAST(:fromDate AS DATETIME) IS NULL OR er.created_date >= :fromDate) " +
            "AND (CAST(:toDate AS DATETIME) IS NULL OR er.created_date <= :toDate) " +
            "AND ((LOWER(er.name) like CONCAT('%', LOWER(:keyword), '%')) " +
            "OR (LOWER(er.code) like CONCAT('%', LOWER(:keyword), '%'))) ", nativeQuery = true)
    Page<ExportReceiptEntity> search(String keyword, Date fromDate, Date toDate, Pageable pageable);
}
