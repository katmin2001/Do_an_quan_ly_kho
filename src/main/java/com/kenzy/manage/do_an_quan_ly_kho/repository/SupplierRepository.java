package com.kenzy.manage.do_an_quan_ly_kho.repository;

import com.kenzy.manage.do_an_quan_ly_kho.entity.SupplierEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface SupplierRepository extends JpaRepository<SupplierEntity, Long> {
    @Query(value = "SELECT * FROM quan_ly_kho.tbl_supplier ts " +
            "WHERE (CAST(:fromDate AS DATETIME) IS NULL OR ts.created_date >= :fromDate) " +
            "AND (CAST(:toDate AS DATETIME) IS NULL OR ts.created_date <= :toDate) " +
            "AND (LOWER(ts.contact_name) like CONCAT('%', LOWER(:keyword), '%')) AND ts.status = true ",
            nativeQuery = true)
    Page<SupplierEntity> search(String keyword, Date fromDate, Date toDate, Pageable pageable);

    boolean existsSupplierEntityById(Long id);
}
