package com.kenzy.manage.do_an_quan_ly_kho.service;

import com.kenzy.manage.do_an_quan_ly_kho.entity.UserEntity;
import com.kenzy.manage.do_an_quan_ly_kho.entity.constant.Result;
import com.kenzy.manage.do_an_quan_ly_kho.model.request.UserRequest;
import com.kenzy.manage.do_an_quan_ly_kho.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService extends BaseService {
    @Autowired
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public ResponseEntity<Result> createAndEdit(UserRequest request) {
        try {
            return ResponseEntity.ok(new Result("SUCCESS", "OK", ))
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Result("NOT FOUND USER", "NOT_FOUND", null));
        }
    }

    private UserEntity saveUser(UserRequest request) {
        UserEntity user = null;
        if (request.getId() == null) {
            user = new UserEntity();
        } else {
            user = userRepository.findById(request.getId()).orElse(null);
            if (user == null) {
                throw new NullPointerException("Not found user!");
            }
            user.setUpdatedDate(new Date());
        }
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
    }
}
