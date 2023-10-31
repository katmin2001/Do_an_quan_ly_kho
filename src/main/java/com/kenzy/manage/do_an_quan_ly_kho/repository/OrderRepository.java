package com.kenzy.manage.do_an_quan_ly_kho.repository;

import com.kenzy.manage.do_an_quan_ly_kho.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    @Query(value = "SELECT * " +
            "FROM quan_ly_kho.tbl_order toa " +
            "WHERE (CAST(:fromDate AS DATETIME) IS NULL OR toa.created_date >= :fromDate) " +
            "AND (CAST(:toDate AS DATETIME) IS NULL OR toa.created_date <= :toDate) ", nativeQuery = true)
    Page<OrderEntity> search(Date fromDate, Date toDate, Pageable pageable);
}
