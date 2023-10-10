package com.kenzy.manage.do_an_quan_ly_kho.repository;

import com.kenzy.manage.do_an_quan_ly_kho.entity.CategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    @Query(value = "SELECT * FROM quan_ly_kho.tbl_category tc " +
            "WHERE (CAST(:fromDate AS DATETIME) IS NULL OR tc.created_date >= :fromDate) " +
            "AND (CAST(:toDate AS DATETIME) IS NULL OR tc.created_date <= :toDate) " +
            "AND (LOWER(tc.name) like CONCAT('%', LOWER(:keyword), '%') or tc.description like CONCAT('%', LOWER(:keyword), '%')) ",
            nativeQuery = true)
    Page<CategoryEntity> search(String keyword, Date fromDate, Date toDate, Pageable pageable);
}
