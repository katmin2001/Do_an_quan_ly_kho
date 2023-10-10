package com.kenzy.manage.do_an_quan_ly_kho.service;

import com.kenzy.manage.do_an_quan_ly_kho.entity.constant.Constants;
import com.kenzy.manage.do_an_quan_ly_kho.model.response.MetaList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseService {
    public static final Logger LOGGER = LoggerFactory.getLogger(BaseService.class);
    protected Pageable buildPageable(MetaList metaList, String sortDefault, boolean isDesc) {
        if (ObjectUtils.isEmpty(metaList)) {
            return Pageable.unpaged();
        }
        Sort sort = Sort.unsorted();
        List<Sort.Order> orders = new ArrayList<>();
        Integer pageNum = metaList.getPageNum();
        Integer pageSize = metaList.getPageSize();
        if (pageNum == null) {
            pageNum = Constants.PAGE_NUM_DEFAULT;
        }
        if (pageSize == null) {
            pageSize = Constants.PAGE_SIZE_DEFAULT;
        }
        String by = null;
        if (metaList.getSortBy() != null) {
            by = metaList.getSortBy();
        }
        if (by != null) {
            Sort.Direction direction = Boolean.TRUE.equals(metaList.getSortDesc()) ? Sort.Direction.DESC : Sort.Direction.ASC;
            Sort.Order order = new Sort.Order(direction, by);
            orders.add(order);
        } else {
            Sort.Order order;
            if (isDesc) {
                order = new Sort.Order(Sort.Direction.DESC, sortDefault);
            } else {
                order = new Sort.Order(Sort.Direction.ASC, sortDefault);
            }
            orders.add(order);
        }
        sort = Sort.by(orders);

        return PageRequest.of(pageNum, pageSize, sort);
    }
    protected MetaList buildMetaList(Pageable pageable, Long total) {
        Integer pageNum = pageable.isUnpaged() ? Constants.PAGE_NUM_DEFAULT : pageable.getPageNumber();
        Integer pageSize = pageable.isUnpaged() ? Constants.PAGE_SIZE_DEFAULT : pageable.getPageSize();
        return MetaList.builder().pageNum(pageNum).pageSize(pageSize).total(total).build();
    }
}
