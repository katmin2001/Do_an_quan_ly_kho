package com.kenzy.manage.do_an_quan_ly_kho.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MetaList {

    private Long total;
    private Integer pageNum;
    private Integer pageSize;
    private String sortBy;
    private Boolean sortDesc;
}
