package com.kenzy.manage.do_an_quan_ly_kho.model.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CategoryRequest {
    private Long id;
    private String name;
    private String description;
}
