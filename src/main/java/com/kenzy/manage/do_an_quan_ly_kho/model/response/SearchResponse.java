package com.kenzy.manage.do_an_quan_ly_kho.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@Data
@AllArgsConstructor
public class SearchResponse<T> {
    private List<T> content;
    private MetaList meta;
}
