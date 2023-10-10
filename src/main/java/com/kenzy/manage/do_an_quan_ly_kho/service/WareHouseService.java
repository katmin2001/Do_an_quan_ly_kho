package com.kenzy.manage.do_an_quan_ly_kho.service;

import com.kenzy.manage.do_an_quan_ly_kho.entity.WareHouseEntity;
import com.kenzy.manage.do_an_quan_ly_kho.entity.constant.Result;
import com.kenzy.manage.do_an_quan_ly_kho.model.request.SearchRequest;
import com.kenzy.manage.do_an_quan_ly_kho.model.request.WareHouseRequest;
import com.kenzy.manage.do_an_quan_ly_kho.model.response.MetaList;
import com.kenzy.manage.do_an_quan_ly_kho.model.response.SearchResponse;
import com.kenzy.manage.do_an_quan_ly_kho.repository.WareHouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class WareHouseService extends BaseService {
    @Autowired
    private WareHouseRepository wareHouseRepository;

    public ResponseEntity<Result> createAndEditWareHouse(WareHouseRequest request) {
        try {
            return ResponseEntity.ok(new Result("SUCCESS", "OK", saveWareHouse(request)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Result("NOT FOUND WAREHOUSE", "NOT_FOUND", null));
        }
    }

    public ResponseEntity<Result> delete(Long id) {
        try {
            WareHouseEntity wareHouse = wareHouseRepository.findById(id).orElseThrow(null);
            wareHouse.setStatus(false);
            wareHouse.setUpdatedDate(new Date());
            return ResponseEntity.ok(new Result("SUCCESS", "OK", wareHouseRepository.save(wareHouse)));
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Result("NOT FOUND WAREHOUSE", "NOT_FOUND", null));
        }
    }

    public ResponseEntity<Result> active(Long id) {
        try {
            WareHouseEntity wareHouse = wareHouseRepository.findById(id).orElseThrow(null);
            wareHouse.setStatus(true);
            wareHouse.setUpdatedDate(new Date());
            return ResponseEntity.ok(new Result("SUCCESS", "OK", wareHouseRepository.save(wareHouse)));
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Result("NOT FOUND WAREHOUSE", "NOT_FOUND", null));
        }
    }

    public ResponseEntity<Result> search(SearchRequest request) {
        MetaList metaList = request.getMeta();
        Pageable pageable = buildPageable(request.getMeta(), "created_date", true);
        Page<WareHouseEntity> page = wareHouseRepository.search(request.getKeyword(), request.getFromDate(), request.getToDate(), pageable);
        metaList.setTotal(page.getTotalElements());
        SearchResponse<WareHouseEntity> response = new SearchResponse<>(page.toList(), metaList);
        return ResponseEntity.ok(new Result("SUCCESS", "OK", response));
    }

    public ResponseEntity<Result> getDetailWareHouse(Long id) {
        try {
            WareHouseEntity wareHouse = wareHouseRepository.findById(id).orElseThrow(null);
            return ResponseEntity.ok(new Result("SUCCESS", "OK", wareHouse));
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Result("NOT FOUND WAREHOUSE", "NOT_FOUND", null));
        }
    }

    private WareHouseEntity saveWareHouse(WareHouseRequest request) {
        WareHouseEntity wareHouse = null;
        if (request.getId() == null) {
            wareHouse = new WareHouseEntity();
        } else {
            wareHouse = wareHouseRepository.findById(request.getId()).orElse(null);
            if (wareHouse == null) {
                throw new NullPointerException("Not found ware house!");
            }
            wareHouse.setUpdatedDate(new Date());
        }
        wareHouse.setWareHouseName(request.getName());
        wareHouse.setDescription(request.getDescription());
        wareHouse.setLocation(request.getLocation());
        return wareHouseRepository.save(wareHouse);
    }
}
