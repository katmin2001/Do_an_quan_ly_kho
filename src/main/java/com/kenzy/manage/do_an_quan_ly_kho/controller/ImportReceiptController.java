package com.kenzy.manage.do_an_quan_ly_kho.controller;

import com.kenzy.manage.do_an_quan_ly_kho.entity.constant.Result;
import com.kenzy.manage.do_an_quan_ly_kho.model.request.ImportReceiptRequest;
import com.kenzy.manage.do_an_quan_ly_kho.model.request.SearchRequest;
import com.kenzy.manage.do_an_quan_ly_kho.service.ImportReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/import_receipt")
public class ImportReceiptController {
    @Autowired
    private ImportReceiptService importReceiptService;

    @PostMapping("/create")
    public ResponseEntity<Result> create(@RequestBody ImportReceiptRequest request) {
        return importReceiptService.createAndUpdate(request);
    }

    @PostMapping("/edit")
    public ResponseEntity<Result> edit(@RequestBody ImportReceiptRequest request) {
        return importReceiptService.createAndUpdate(request);
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<Result> delete(@PathVariable("id") Long id) {
        return importReceiptService.deleteImportReceipt(id);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Result> detail(@PathVariable("id") Long id) {
        return importReceiptService.detailImportReceipt(id);
    }

    @PostMapping("/search")
    public ResponseEntity<Result> search(@RequestBody SearchRequest request){
        return importReceiptService.search(request);
    }
}
