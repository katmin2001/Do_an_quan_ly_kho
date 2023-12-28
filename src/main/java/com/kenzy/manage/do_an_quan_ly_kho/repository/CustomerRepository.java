package com.kenzy.manage.do_an_quan_ly_kho.repository;

import com.kenzy.manage.do_an_quan_ly_kho.entity.CustomerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {
    @Query(value = "SELECT * FROM quan_ly_kho.tbl_customer tc " +
            "WHERE (CAST(:fromDate AS DATETIME) IS NULL OR tc.created_date >= :fromDate) " +
            "AND (CAST(:toDate AS DATETIME) IS NULL OR tc.created_date <= :toDate) " +
            "AND (LOWER(tc.name) like CONCAT('%', LOWER(:keyword), '%')) and tc.status = true ",
            nativeQuery = true)
    Page<CustomerEntity> search(String keyword, Date fromDate, Date toDate, Pageable pageable);
}
