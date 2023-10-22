package com.kenzy.manage.do_an_quan_ly_kho.repository;

import com.kenzy.manage.do_an_quan_ly_kho.entity.ExportReceiptDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExportReceiptDetailRepository extends JpaRepository<ExportReceiptDetailEntity, Long> {
    List<ExportReceiptDetailEntity> findExportReceiptDetailEntitiesByExportReceiptId(Long id);

    @Query(value = "SELECT sum(terd.quantity)\n" +
            "FROM quan_ly_kho.tbl_export_receipt_detail terd\n" +
            "where product_id = :id ", nativeQuery = true)
    long getQuantityProductExportById(Long id);
}
