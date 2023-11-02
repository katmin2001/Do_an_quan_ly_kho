package com.kenzy.manage.do_an_quan_ly_kho.controller;

import com.kenzy.manage.do_an_quan_ly_kho.entity.constant.Result;
import com.kenzy.manage.do_an_quan_ly_kho.model.request.BillRequest;
import com.kenzy.manage.do_an_quan_ly_kho.model.request.SearchRequest;
import com.kenzy.manage.do_an_quan_ly_kho.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bill")
public class BillController {
    @Autowired
    private BillService billService;

    @PostMapping("/create")
    public ResponseEntity<Result> create(@RequestBody BillRequest request) {
        return billService.createAndEdit(request);
    }

    @PostMapping("/edit")
    public ResponseEntity<Result> edit(@RequestBody BillRequest request) {
        return billService.createAndEdit(request);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Result> detail(@PathVariable("id") Long id) {
        return billService.detailBill(id);
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<Result> delete(@PathVariable("id") Long id) {
        return billService.delete(id);
    }

    @PostMapping("/search")
    public ResponseEntity<Result> search(@RequestBody SearchRequest request) {
        return billService.searchBill(request);
    }
}
