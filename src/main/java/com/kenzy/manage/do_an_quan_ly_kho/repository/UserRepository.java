package com.kenzy.manage.do_an_quan_ly_kho.repository;

import com.kenzy.manage.do_an_quan_ly_kho.entity.UserEntity;
import com.kenzy.manage.do_an_quan_ly_kho.model.response.IGetUserResponse;
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

    @Query(value = "SELECT tu.id, tu.username, tu.name, tu.email, tu.address, tu.phone, tu.role as role, tu.avatar as avatar FROM quan_ly_kho.tbl_user tu " +
            "WHERE (CAST(:fromDate AS DATETIME) IS NULL OR tu.created_date >= :fromDate) " +
            "AND (CAST(:toDate AS DATETIME) IS NULL OR tu.created_date <= :toDate) " +
            "AND (LOWER(tu.name) like CONCAT('%', LOWER(:keyword), '%') or tu.username like CONCAT('%', LOWER(:keyword), '%')) AND tu.status = true AND tu.username != 'admin' ",
            nativeQuery = true)
    Page<IGetUserResponse> search(String keyword, Date fromDate, Date toDate, Pageable pageable);
}
