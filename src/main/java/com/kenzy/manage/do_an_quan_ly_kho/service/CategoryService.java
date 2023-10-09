package com.kenzy.manage.do_an_quan_ly_kho.service;

import com.kenzy.manage.do_an_quan_ly_kho.model.entity.CategoryEntity;
import com.kenzy.manage.do_an_quan_ly_kho.model.entity.constant.Result;
import com.kenzy.manage.do_an_quan_ly_kho.model.request.CategoryRequest;
import com.kenzy.manage.do_an_quan_ly_kho.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Transactional
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public ResponseEntity<Result> createAndEditCategory(CategoryRequest categoryRequest) {
        if (categoryRequest.getId() != null) {
            CategoryEntity category = categoryRepository.findById(categoryRequest.getId()).orElseThrow(null);
            if (categoryRequest.getName() != null) {
                category.setName(categoryRequest.getName());
            }
            if (categoryRequest.getDescription() != null) {
                category.setDescription(categoryRequest.getDescription());
            }
//            if(categoryRequest.getCategoryImage() != null){
//                category.setCategoryImage(categoryRequest.getCategoryImage());
//            }
            category.setUpdatedDate(new Date());
            return ResponseEntity.ok(new Result("SUCCESS", "OK", categoryRepository.save(category)));
        }
        CategoryEntity category = new CategoryEntity();
        category.setName(categoryRequest.getName());
        category.setDescription(categoryRequest.getDescription());
//        category.setCategoryImage(categoryRequest.getCategoryImage());
        return ResponseEntity.ok(new Result("SUCCESS", "OK", categoryRepository.save(category)));
    }

}
