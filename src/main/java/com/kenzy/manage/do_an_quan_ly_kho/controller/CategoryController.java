package com.kenzy.manage.do_an_quan_ly_kho.controller;

import com.kenzy.manage.do_an_quan_ly_kho.model.entity.constant.Result;
import com.kenzy.manage.do_an_quan_ly_kho.model.request.CategoryRequest;
import com.kenzy.manage.do_an_quan_ly_kho.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @PostMapping("/create")
    public ResponseEntity<Result> createCategory(@RequestBody CategoryRequest category){
        return categoryService.createAndEditCategory(category);
    }
    @PostMapping("/edit")
    public ResponseEntity<Result> editCategory(@RequestBody CategoryRequest category){
        return categoryService.createAndEditCategory(category);
    }
}
