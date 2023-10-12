package com.kenzy.manage.do_an_quan_ly_kho.repository;

import com.kenzy.manage.do_an_quan_ly_kho.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);

    boolean existsUserByUsername(String username);

    @Query(value = "SELECT * FROM quan_ly_kho.tbl_user tu " +
            "WHERE (CAST(:fromDate AS DATETIME) IS NULL OR tu.created_date >= :fromDate) " +
            "AND (CAST(:toDate AS DATETIME) IS NULL OR tu.created_date <= :toDate) " +
            "AND (LOWER(tu.name) like CONCAT('%', LOWER(:keyword), '%') or tu.username like CONCAT('%', LOWER(:keyword), '%')) AND tu.status = true AND tu.role != 'ADMIN' ",
            nativeQuery = true)
    Page<UserEntity> search(String keyword, Date fromDate, Date toDate, Pageable pageable);
}
