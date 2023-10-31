package com.kenzy.manage.do_an_quan_ly_kho.repository;

import com.kenzy.manage.do_an_quan_ly_kho.entity.PaymentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
    @Query(value = "SELECT * FROM quan_ly_kho.tbl_payment tp " +
            "WHERE (CAST(:fromDate AS DATETIME) IS NULL OR tp.created_date >= :fromDate) " +
            "AND (CAST(:toDate AS DATETIME) IS NULL OR tp.created_date <= :toDate) ", nativeQuery = true)
    Page<PaymentEntity> search(Date fromDate, Date toDate, Pageable pageable);
}
