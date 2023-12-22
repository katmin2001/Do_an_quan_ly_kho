package com.kenzy.manage.do_an_quan_ly_kho.repository;

import com.kenzy.manage.do_an_quan_ly_kho.entity.ProductEntity;
import com.kenzy.manage.do_an_quan_ly_kho.model.response.IGetProductResponse;
import com.kenzy.manage.do_an_quan_ly_kho.model.response.IGetStatisticByCategoryOrProduct;
import com.kenzy.manage.do_an_quan_ly_kho.model.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    @Query(value = "SELECT * " +
            "FROM quan_ly_kho.tbl_product tp " +
            "WHERE (CAST(:fromDate AS DATETIME) IS NULL OR tp.created_date >= :fromDate) " +
            "AND (CAST(:toDate AS DATETIME) IS NULL OR tp.created_date <= :toDate) " +
            "AND (LOWER(tp.product_name) like CONCAT('%', LOWER(:keyword), '%')) " +
            "AND (:categoryId IS NULL OR tp.category_id <= :categoryId) " +
            "AND (:supplierId IS NULL OR tp.supplier_id <= :supplierId) AND tp.status = true ", nativeQuery = true)
    Page<ProductEntity> search(String keyword, Date fromDate, Date toDate, Long categoryId, Long supplierId, Pageable pageable);
    @Query(value = "SELECT\n" +
            "  tp.product_name AS name,\n" +
            "  COALESCE(SUM(terd.total_price), 0) AS revenue,\n" +
            "  COALESCE(SUM(tird.total_price), 0) AS costs\n" +
            "FROM\n" +
            "  quan_ly_kho.tbl_product tp\n" +
            "LEFT JOIN\n" +
            "  quan_ly_kho.tbl_export_receipt_detail terd ON tp.id = terd.product_id AND terd.status = true\n" +
            "LEFT JOIN\n" +
            "  quan_ly_kho.tbl_import_receipt_detail tird ON tp.id = tird.product_id\n" +
            "GROUP BY\n" +
            "  tp.product_name ", nativeQuery = true)
    List<IGetStatisticByCategoryOrProduct> getStatisticByProduct();
}
