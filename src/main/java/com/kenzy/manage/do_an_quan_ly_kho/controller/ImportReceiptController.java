package com.kenzy.manage.do_an_quan_ly_kho.controller;

import com.kenzy.manage.do_an_quan_ly_kho.entity.constant.Result;
import com.kenzy.manage.do_an_quan_ly_kho.model.request.ImportReceiptRequest;
import com.kenzy.manage.do_an_quan_ly_kho.service.ImportReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/import_receipt")
public class ImportReceiptController {
    @Autowired
    private ImportReceiptService importReceiptService;

    @PostMapping("/create")
    public ResponseEntity<Result> create(@RequestBody ImportReceiptRequest request) {
        return importReceiptService.createAndUpdate(request);
    }
}
