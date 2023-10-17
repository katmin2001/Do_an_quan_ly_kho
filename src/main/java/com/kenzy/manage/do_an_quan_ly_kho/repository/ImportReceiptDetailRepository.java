package com.kenzy.manage.do_an_quan_ly_kho.repository;

import com.kenzy.manage.do_an_quan_ly_kho.entity.ImportReceiptDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImportReceiptDetailRepository extends JpaRepository<ImportReceiptDetailEntity, Long> {
    ImportReceiptDetailEntity findByImportReceiptId(Long id);
    List<ImportReceiptDetailEntity> getImportReceiptDetailEntitiesByImportReceiptId(Long id);
}
