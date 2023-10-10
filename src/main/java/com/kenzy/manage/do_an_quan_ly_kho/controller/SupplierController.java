package com.kenzy.manage.do_an_quan_ly_kho.controller;

import com.kenzy.manage.do_an_quan_ly_kho.entity.constant.Result;
import com.kenzy.manage.do_an_quan_ly_kho.model.request.SearchRequest;
import com.kenzy.manage.do_an_quan_ly_kho.model.request.SupplierRequest;
import com.kenzy.manage.do_an_quan_ly_kho.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/supplier")
public class SupplierController {
    @Autowired
    private SupplierService supplierService;

    @PostMapping("/create")
    public ResponseEntity<Result> create(@RequestBody SupplierRequest request) {
        return supplierService.createAndEditSupplier(request);
    }

    @PostMapping("/edit")
    public ResponseEntity<Result> edit(@RequestBody SupplierRequest request) {
        return supplierService.createAndEditSupplier(request);
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<Result> delete(@PathVariable("id") Long id) {
        return supplierService.delete(id);
    }

    @GetMapping("/active/{id}")
    public ResponseEntity<Result> active(@PathVariable("id") Long id) {
        return supplierService.active(id);
    }

    @PostMapping("/search")
    public ResponseEntity<Result> search(@RequestBody SearchRequest request) {
        return supplierService.search(request);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Result> detail(@PathVariable("id") Long id){
        return supplierService.getDetailSupplier(id);
    }
}
