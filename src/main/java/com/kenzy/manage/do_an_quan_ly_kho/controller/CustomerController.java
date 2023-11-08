package com.kenzy.manage.do_an_quan_ly_kho.controller;

import com.kenzy.manage.do_an_quan_ly_kho.entity.constant.Result;
import com.kenzy.manage.do_an_quan_ly_kho.model.request.CustomerRequest;
import com.kenzy.manage.do_an_quan_ly_kho.model.request.SearchRequest;
import com.kenzy.manage.do_an_quan_ly_kho.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @PostMapping("/create")
    public ResponseEntity<Result> create(@RequestBody CustomerRequest request) {
        return customerService.createAndEditCustomer(request);
    }

    @PostMapping("/edit")
    public ResponseEntity<Result> edit(@RequestBody CustomerRequest request) {
        return customerService.createAndEditCustomer(request);
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<Result> delete(@PathVariable("id") Long id) {
        return customerService.delete(id);
    }

    @GetMapping("/inactive/{id}")
    public ResponseEntity<Result> inactive(@PathVariable("id") Long id) {
        return customerService.inactive(id);
    }

    @GetMapping("/active/{id}")
    public ResponseEntity<Result> active(@PathVariable("id") Long id) {
        return customerService.active(id);
    }

    @PostMapping("/search")
    public ResponseEntity<Result> search(@RequestBody SearchRequest request) {
        return customerService.search(request);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Result> detail(@PathVariable("id") Long id) {
        return customerService.getDetailCustomer(id);
    }
}
