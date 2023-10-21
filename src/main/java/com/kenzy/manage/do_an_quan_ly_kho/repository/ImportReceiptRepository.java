package com.kenzy.manage.do_an_quan_ly_kho.repository;

import com.kenzy.manage.do_an_quan_ly_kho.entity.ImportReceiptEntity;
import com.kenzy.manage.do_an_quan_ly_kho.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface ImportReceiptRepository extends JpaRepository<ImportReceiptEntity, Long> {
    @Query(value = "SELECT * " +
            "FROM quan_ly_kho.tbl_import_receipt tir " +
            "WHERE (CAST(:fromDate AS DATETIME) IS NULL OR tir.created_date >= :fromDate) " +
            "AND (CAST(:toDate AS DATETIME) IS NULL OR tir.created_date <= :toDate) " +
            "AND (LOWER(tir.name) like CONCAT('%', LOWER(:keyword), '%')) ", nativeQuery = true)
    Page<ImportReceiptEntity> search(String keyword, Date fromDate, Date toDate, Pageable pageable);
}
