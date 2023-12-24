package com.kenzy.manage.do_an_quan_ly_kho.controller;

import com.kenzy.manage.do_an_quan_ly_kho.entity.constant.Result;
import com.kenzy.manage.do_an_quan_ly_kho.model.request.ProductRequest;
import com.kenzy.manage.do_an_quan_ly_kho.model.request.ProductSearchRequest;
import com.kenzy.manage.do_an_quan_ly_kho.model.request.SearchRequest;
import com.kenzy.manage.do_an_quan_ly_kho.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/create")
    public ResponseEntity<Result> create(@RequestPart ProductRequest request,
                                         @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        return productService.createAndEditProduct(request, files);
    }

    @PostMapping("/edit")
    public ResponseEntity<Result> edit(@RequestPart ProductRequest request,
                                       @RequestPart List<MultipartFile> files) {
        return productService.createAndEditProduct(request, files);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Result> detail(@PathVariable("id") Long id) {
        return productService.detail(id);
    }

    @PostMapping("/search")
    public ResponseEntity<Result> search(@RequestBody ProductSearchRequest request) {
        return productService.search(request);
    }

    @PostMapping("/inactive/{id}")
    public ResponseEntity<Result> inactive(@PathVariable("id") Long id) {
        return productService.inactiveProduct(id);
    }

    @PostMapping("/active/{id}")
    public ResponseEntity<Result> active(@PathVariable("id") Long id) {
        return productService.activeProduct(id);
    }
}
