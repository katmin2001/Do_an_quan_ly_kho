package com.kenzy.manage.do_an_quan_ly_kho.repository;

import com.kenzy.manage.do_an_quan_ly_kho.entity.ExportReceiptDetailEntity;
import com.kenzy.manage.do_an_quan_ly_kho.model.response.IGetStatisticByCategoryOrProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExportReceiptDetailRepository extends JpaRepository<ExportReceiptDetailEntity, Long> {
    List<ExportReceiptDetailEntity> findExportReceiptDetailEntitiesByExportReceiptId(Long id);

    @Query(value = "SELECT sum(terd.quantity)\n" +
            "FROM quan_ly_kho.tbl_export_receipt_detail terd\n" +
            "where product_id = :id and status = true ", nativeQuery = true)
    Integer getQuantityProductExportById(Long id);

    List<ExportReceiptDetailEntity> getExportReceiptDetailEntitiesByProductId(Long id);

    List<ExportReceiptDetailEntity> findExportReceiptDetailEntitiesByStatus(Boolean status);
//    @Query(value = "SELECT tp.product_name as name,\n" +
//            "sum(terd.total_price) as revenue,\n" +
//            "sum(tird.total_price) as costs\n" +
//            "FROM quan_ly_kho.tbl_export_receipt_detail terd\n" +
//            "JOIN quan_ly_kho.tbl_product tp ON tp.id = terd.product_id\n" +
//            "JOIN quan_ly_kho.tbl_import_receipt_detail tird ON tp.id = tird.product_id\n" +
//            "where terd.status = true\n" +
//            "group by tp.product_name ", nativeQuery = true)
//    List<IGetStatisticByCategoryOrProduct> getStatisticByProduct();
}
