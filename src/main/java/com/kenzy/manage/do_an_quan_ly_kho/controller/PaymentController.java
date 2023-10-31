package com.kenzy.manage.do_an_quan_ly_kho.controller;

import com.kenzy.manage.do_an_quan_ly_kho.entity.constant.Result;
import com.kenzy.manage.do_an_quan_ly_kho.model.request.PaymentRequest;
import com.kenzy.manage.do_an_quan_ly_kho.model.request.SearchRequest;
import com.kenzy.manage.do_an_quan_ly_kho.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @PostMapping("/create")
    public ResponseEntity<Result> create(@RequestBody PaymentRequest request) {
        return paymentService.createAndEdit(request);
    }

    @PostMapping("/edit")
    public ResponseEntity<Result> edit(@RequestBody PaymentRequest request) {
        return paymentService.createAndEdit(request);
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<Result> delete(@PathVariable("id") Long id) {
        return paymentService.deletePayment(id);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Result> detail(@PathVariable("id") Long id) {
        return paymentService.detailPayment(id);
    }

    @PostMapping("/search")
    public ResponseEntity<Result> search(@RequestBody SearchRequest request) {
        return paymentService.searchPayment(request);
    }
}
