package com.kenzy.manage.do_an_quan_ly_kho.controller;

import com.kenzy.manage.do_an_quan_ly_kho.entity.constant.Result;
import com.kenzy.manage.do_an_quan_ly_kho.model.request.CategoryRequest;
import com.kenzy.manage.do_an_quan_ly_kho.model.request.SearchRequest;
import com.kenzy.manage.do_an_quan_ly_kho.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping("/create")
    public ResponseEntity<Result> createCategory(@RequestPart CategoryRequest category,
                                                 @RequestPart MultipartFile file) throws IOException {
        return categoryService.createAndEditCategory(category, file);
    }

    @PostMapping("/edit")
    public ResponseEntity<Result> editCategory(@RequestPart CategoryRequest category,
                                               @RequestPart MultipartFile file) throws IOException {
        return categoryService.createAndEditCategory(category, file);
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<Result> deleteCategory(@PathVariable("id") Long id) {
        return categoryService.deleteCategory(id);
    }

    @PostMapping("/search")
    public ResponseEntity<Result> searchCategory(@RequestBody SearchRequest request) {
        return categoryService.searchCategory(request);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Result> getDetailCategory(@PathVariable("id") Long id) {
        return categoryService.getDetailCategory(id);
    }
}
