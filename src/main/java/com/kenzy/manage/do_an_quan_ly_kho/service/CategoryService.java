package com.kenzy.manage.do_an_quan_ly_kho.service;

import com.kenzy.manage.do_an_quan_ly_kho.entity.CategoryEntity;
import com.kenzy.manage.do_an_quan_ly_kho.entity.constant.Result;
import com.kenzy.manage.do_an_quan_ly_kho.model.request.CategoryRequest;
import com.kenzy.manage.do_an_quan_ly_kho.model.request.SearchRequest;
import com.kenzy.manage.do_an_quan_ly_kho.model.response.MetaList;
import com.kenzy.manage.do_an_quan_ly_kho.model.response.SearchResponse;
import com.kenzy.manage.do_an_quan_ly_kho.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;

@Service
@Transactional
public class CategoryService extends BaseService {
    private final String UPLOAD_DIR = "upload/category-image";
    @Autowired
    private CategoryRepository categoryRepository;

    public ResponseEntity<Result> createAndEditCategory(CategoryRequest categoryRequest, MultipartFile file) throws IOException {
        try {
            return ResponseEntity.ok(new Result("SUCCESS", "OK", saveCategory(categoryRequest, file)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Result("NOT FOUND CATEGORY", "NOT_FOUND", null));
        }
    }

    public ResponseEntity<Result> deleteCategory(Long categoryId) {
        try {
            CategoryEntity category = categoryRepository.findById(categoryId).orElseThrow(null);
            FileService.deleteFile(category.getCategoryImage());
            categoryRepository.delete(category);
            return ResponseEntity.ok(new Result("SUCCESS", "OK", null));
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Result("NOT FOUND CATEGORY", "NOT_FOUND", null));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<Result> searchCategory(SearchRequest request) {
        MetaList metaList = request.getMeta();
        Pageable pageable = buildPageable(request.getMeta(), "created_date", true);
        Page<CategoryEntity> page = categoryRepository.search(request.getKeyword(), request.getFromDate(), request.getToDate(), pageable);
        metaList.setTotal(page.getTotalElements());
        SearchResponse<CategoryEntity> response = new SearchResponse<>(page.toList(), metaList);
        return ResponseEntity.ok(new Result("SUCCESS", "OK", response));
    }

    public ResponseEntity<Result> getDetailCategory(Long id) {
        try {
            CategoryEntity category = categoryRepository.findById(id).orElseThrow(null);
            return ResponseEntity.ok(new Result("SUCCESS", "OK", category));
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Result("NOT FOUND CATEGORY", "NOT_FOUND", null));
        }
    }

    private CategoryEntity saveCategory(CategoryRequest categoryRequest, MultipartFile file) throws IOException {
        CategoryEntity category = null;
        if (categoryRequest.getId() == null) {
            category = new CategoryEntity();
        } else {
            category = categoryRepository.findById(categoryRequest.getId()).orElse(null);
            if (category == null) {
                throw new NullPointerException("Not found category!");
            }
            FileService.deleteFile(category.getCategoryImage());
            category.setUpdatedDate(new Date());
        }
        category.setName(categoryRequest.getName());
        category.setDescription(categoryRequest.getDescription());
        category.setCategoryImage(FileService.uploadFile(file, UPLOAD_DIR));
        return categoryRepository.save(category);
    }
}
