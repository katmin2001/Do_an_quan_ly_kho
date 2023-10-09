package com.kenzy.manage.do_an_quan_ly_kho.repository;

import com.kenzy.manage.do_an_quan_ly_kho.model.entity.OrderDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetailEntity, Long> {
}
