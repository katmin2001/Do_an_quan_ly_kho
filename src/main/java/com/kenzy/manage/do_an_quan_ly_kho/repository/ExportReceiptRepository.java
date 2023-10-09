package com.kenzy.manage.do_an_quan_ly_kho.repository;

import com.kenzy.manage.do_an_quan_ly_kho.model.entity.ExportReceiptEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExportReceiptRepository extends JpaRepository<ExportReceiptEntity, Long> {
}
