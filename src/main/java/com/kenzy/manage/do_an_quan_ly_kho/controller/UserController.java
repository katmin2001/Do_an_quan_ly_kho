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
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/api/admin/user")
public class UserController {
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<Result> create(@RequestPart UserRequest request,
                                         @RequestPart MultipartFile file) {
        return userService.create(request, file);
    }

    @PostMapping("/edit")
    public ResponseEntity<Result> edit(@RequestPart UserEditRequest request,
                                       @RequestPart MultipartFile file) {
        return userService.edit(request, file);
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
