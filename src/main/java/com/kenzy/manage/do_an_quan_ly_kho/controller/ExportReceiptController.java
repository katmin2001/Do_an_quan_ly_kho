package com.kenzy.manage.do_an_quan_ly_kho.controller;

import com.kenzy.manage.do_an_quan_ly_kho.entity.constant.Result;
import com.kenzy.manage.do_an_quan_ly_kho.model.request.ExportReceiptRequest;
import com.kenzy.manage.do_an_quan_ly_kho.model.request.SearchRequest;
import com.kenzy.manage.do_an_quan_ly_kho.service.ExportReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/export_receipt")
public class ExportReceiptController {
    @Autowired
    private ExportReceiptService exportReceiptService;

    @PostMapping("/create")
    public ResponseEntity<Result> create(@RequestBody ExportReceiptRequest request) {
        return exportReceiptService.createAndEdit(request);
    }

    @PostMapping("/edit")
    public ResponseEntity<Result> edit(@RequestBody ExportReceiptRequest request) {
        return exportReceiptService.createAndEdit(request);
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<Result> delete(@PathVariable("id") Long id) {
        return exportReceiptService.delete(id);
    }

    @GetMapping("/restore/{id}")
    public ResponseEntity<Result> restore(@PathVariable("id") Long id) {
        return exportReceiptService.restore(id);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Result> detail(@PathVariable("id") Long id) {
        return exportReceiptService.detail(id);
    }

    @PostMapping("/search")
    public ResponseEntity<Result> search(@RequestBody SearchRequest request) {
        return exportReceiptService.search(request);
    }
}
