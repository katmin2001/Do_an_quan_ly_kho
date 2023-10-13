package com.kenzy.manage.do_an_quan_ly_kho.repository;

import com.kenzy.manage.do_an_quan_ly_kho.entity.ProductEntity;
import com.kenzy.manage.do_an_quan_ly_kho.model.response.IGetProductResponse;
import com.kenzy.manage.do_an_quan_ly_kho.model.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    @Query(value = "SELECT * " +
            "FROM quan_ly_kho.tbl_product tp " +
            "WHERE (CAST(:fromDate AS DATETIME) IS NULL OR tp.created_date >= :fromDate) " +
            "AND (CAST(:toDate AS DATETIME) IS NULL OR tp.created_date <= :toDate) " +
            "AND (LOWER(tp.product_name) like CONCAT('%', LOWER(:keyword), '%')) AND tp.status = true ", nativeQuery = true)
    Page<ProductEntity> search(String keyword, Date fromDate, Date toDate, Pageable pageable);
}
