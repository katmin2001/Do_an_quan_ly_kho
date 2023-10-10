package com.kenzy.manage.do_an_quan_ly_kho.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kenzy.manage.do_an_quan_ly_kho.model.response.MetaList;
import lombok.Data;

import java.util.Date;

@Data
public class SearchRequest {
    private String keyword;
    @JsonFormat(pattern = "yyyy/MM/dd")
    private Date fromDate;
    @JsonFormat(pattern = "yyyy/MM/dd")
    private Date toDate;
    private MetaList meta;
}
