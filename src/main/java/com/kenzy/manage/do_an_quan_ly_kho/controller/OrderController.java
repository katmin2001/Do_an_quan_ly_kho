package com.kenzy.manage.do_an_quan_ly_kho.controller;

import com.kenzy.manage.do_an_quan_ly_kho.entity.constant.Result;
import com.kenzy.manage.do_an_quan_ly_kho.model.request.OrderRequest;
import com.kenzy.manage.do_an_quan_ly_kho.model.request.SearchRequest;
import com.kenzy.manage.do_an_quan_ly_kho.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<Result> create(@RequestBody OrderRequest request) {
        return orderService.createAndUpdate(request);
    }

    @PostMapping("/edit")
    public ResponseEntity<Result> edit(@RequestBody OrderRequest request) {
        return orderService.createAndUpdate(request);
    }

    @GetMapping("/cancel/{id}")
    public ResponseEntity<Result> cancel(@PathVariable("id") Long id) {
        return orderService.cancelOrder(id);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Result> detail(@PathVariable("id") Long id) {
        return orderService.detailOrder(id);
    }

    @PostMapping("/search")
    public ResponseEntity<Result> search(@RequestBody SearchRequest request) {
        return orderService.searchOrder(request);
    }
}
