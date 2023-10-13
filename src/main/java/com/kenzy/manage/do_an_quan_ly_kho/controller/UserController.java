package com.kenzy.manage.do_an_quan_ly_kho.controller;

import com.kenzy.manage.do_an_quan_ly_kho.auth.AuthenticationService;
import com.kenzy.manage.do_an_quan_ly_kho.entity.constant.Result;
import com.kenzy.manage.do_an_quan_ly_kho.model.request.SearchRequest;
import com.kenzy.manage.do_an_quan_ly_kho.model.request.UserEditRequest;
import com.kenzy.manage.do_an_quan_ly_kho.model.request.UserRequest;
import com.kenzy.manage.do_an_quan_ly_kho.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/admin/user")
public class UserController {
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<Result> create(@RequestBody UserRequest request) {
        return userService.create(request);
    }

    @PostMapping("/edit")
    public ResponseEntity<Result> edit(@RequestBody UserEditRequest request) {
        return userService.edit(request);
    }

    @PostMapping("/inactive/{id}")
    public ResponseEntity<Result> inactive(@PathVariable("id") Long id) {
        return userService.inactive(id);
    }

    @PostMapping("/active/{id}")
    public ResponseEntity<Result> active(@PathVariable("id") Long id) {
        return userService.active(id);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Result> detail(@PathVariable("id") Long id) {
        return userService.detail(id);
    }

    @PostMapping("/search")
    public ResponseEntity<Result> search(@RequestBody SearchRequest request) {
        return userService.searchUser(request);
    }
}
