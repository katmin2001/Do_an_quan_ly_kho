package com.kenzy.manage.do_an_quan_ly_kho.repository;

import com.kenzy.manage.do_an_quan_ly_kho.entity.CategoryEntity;
import com.kenzy.manage.do_an_quan_ly_kho.model.response.IGetStatisticByCategoryOrProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    boolean existsCategoryEntityById(Long id);

    @Query(value = "SELECT * FROM quan_ly_kho.tbl_category tc " +
            "WHERE (CAST(:fromDate AS DATETIME) IS NULL OR tc.created_date >= :fromDate) " +
            "AND (CAST(:toDate AS DATETIME) IS NULL OR tc.created_date <= :toDate) " +
            "AND (LOWER(tc.name) like CONCAT('%', LOWER(:keyword), '%') or tc.description like CONCAT('%', LOWER(:keyword), '%')) ",
            nativeQuery = true)
    Page<CategoryEntity> search(String keyword, Date fromDate, Date toDate, Pageable pageable);

    @Query(value = "SELECT\n" +
            "  tc.name AS name,\n" +
            "  COALESCE(SUM(terd.total_price), 0) AS revenue,\n" +
            "  COALESCE(SUM(tird.total_price), 0) AS costs\n" +
            "FROM\n" +
            "  quan_ly_kho.tbl_category tc\n" +
            "LEFT JOIN\n" +
            "  quan_ly_kho.tbl_product tp ON tc.id = tp.category_id\n" +
            "LEFT JOIN\n" +
            "  quan_ly_kho.tbl_export_receipt_detail terd ON tp.id = terd.product_id AND terd.status = true\n" +
            "LEFT JOIN\n" +
            "  quan_ly_kho.tbl_import_receipt_detail tird ON tp.id = tird.product_id\n" +
            "GROUP BY\n" +
            "  tc.name ", nativeQuery = true)
    List<IGetStatisticByCategoryOrProduct> getStatisticByCategory();
}
