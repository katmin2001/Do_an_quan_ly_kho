package com.kenzy.manage.do_an_quan_ly_kho.service;

import com.kenzy.manage.do_an_quan_ly_kho.entity.UserEntity;
import com.kenzy.manage.do_an_quan_ly_kho.entity.constant.Result;
import com.kenzy.manage.do_an_quan_ly_kho.entity.constant.Role;
import com.kenzy.manage.do_an_quan_ly_kho.model.request.SearchRequest;
import com.kenzy.manage.do_an_quan_ly_kho.model.request.UserEditRequest;
import com.kenzy.manage.do_an_quan_ly_kho.model.request.UserRequest;
import com.kenzy.manage.do_an_quan_ly_kho.model.response.IGetUserResponse;
import com.kenzy.manage.do_an_quan_ly_kho.model.response.MetaList;
import com.kenzy.manage.do_an_quan_ly_kho.model.response.SearchResponse;
import com.kenzy.manage.do_an_quan_ly_kho.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService extends BaseService {
    @Autowired
    private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<Result> create(UserRequest request) {
        try {
            return ResponseEntity.ok(new Result("SUCCESS", "OK", createUser(request)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new Result("USERNAME ALREADY EXIST!", "CONFLICT", null));
        }
    }

    public ResponseEntity<Result> edit(UserEditRequest request) {
        try {
            return ResponseEntity.ok(new Result("SUCCESS", "OK", editUser(request)));
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Result("NOT FOUND USER", "NOT_FOUND", null));
        }
    }

    public ResponseEntity<Result> inactive(Long id) {
        try {
            return ResponseEntity.ok(new Result("SUCCESS", "OK", inactiveUser(id)));
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Result("NOT FOUND USER", "NOT_FOUND", null));
        }
    }

    public ResponseEntity<Result> active(Long id) {
        try {
            return ResponseEntity.ok(new Result("SUCCESS", "OK", activeUser(id)));
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Result("NOT FOUND USER", "NOT_FOUND", null));
        }
    }

    public ResponseEntity<Result> detail(Long id) {
        try {
            UserEntity user = userRepository.findById(id).orElseThrow(null);
            if (user.getRole().equals(Role.ADMIN)) {
                throw new NullPointerException();
            }
            return ResponseEntity.ok(new Result("SUCCESS", "OK", user));
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Result("NOT FOUND USER", "NOT_FOUND", null));
        }
    }

    public ResponseEntity<Result> searchUser(SearchRequest request) {
        MetaList metaList = request.getMeta();
        Pageable pageable = buildPageable(request.getMeta(), "created_date", true);
        Page<IGetUserResponse> page = userRepository.search(request.getKeyword(), request.getFromDate(), request.getToDate(), pageable);
        metaList.setTotal(page.getTotalElements());
        SearchResponse<IGetUserResponse> response = new SearchResponse<>(page.toList(), metaList);
        return ResponseEntity.ok(new Result("SUCCESS", "OK", response));
    }

    private UserEntity createUser(UserRequest request) {
        UserEntity user = new UserEntity();
        if (userRepository.existsUserByUsername(request.getUsername())) {
            throw new RuntimeException();
        }
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());
        user.setPhone(request.getPhone());
        user.setEmail(request.getEmail());
        user.setAddress(request.getAddress());
        user.setRole(Role.ADMIN);
        return userRepository.save(user);
    }

    private UserEntity editUser(UserEditRequest request) {
        UserEntity user = userRepository.findById(request.getId()).orElseThrow(null);
        if (user == null || user.getRole().equals(Role.ADMIN)) {
            throw new NullPointerException();
        }
        user.setName(request.getName());
        user.setPhone(request.getPhone());
        user.setEmail(request.getEmail());
        user.setAddress(request.getAddress());
        user.setUpdatedDate(new Date());
        return userRepository.save(user);
    }

    private UserEntity inactiveUser(Long id) {
        UserEntity user = userRepository.findById(id).orElseThrow(null);
        if (user == null || user.getRole().equals(Role.ADMIN)) {
            throw new NullPointerException();
        }
        user.setStatus(false);
        user.setUpdatedDate(new Date());
        return userRepository.save(user);
    }

    private UserEntity activeUser(Long id) {
        UserEntity user = userRepository.findById(id).orElseThrow(null);
        if (user == null || user.getRole().equals(Role.ADMIN)) {
            throw new NullPointerException();
        }
        user.setStatus(true);
        user.setUpdatedDate(new Date());
        return userRepository.save(user);
    }

}