package com.kenzy.manage.do_an_quan_ly_kho.service;

import com.kenzy.manage.do_an_quan_ly_kho.entity.SupplierEntity;
import com.kenzy.manage.do_an_quan_ly_kho.entity.constant.Result;
import com.kenzy.manage.do_an_quan_ly_kho.model.request.SearchRequest;
import com.kenzy.manage.do_an_quan_ly_kho.model.request.SupplierRequest;
import com.kenzy.manage.do_an_quan_ly_kho.model.response.MetaList;
import com.kenzy.manage.do_an_quan_ly_kho.model.response.SearchResponse;
import com.kenzy.manage.do_an_quan_ly_kho.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SupplierService extends BaseService {
    @Autowired
    private SupplierRepository supplierRepository;

    public ResponseEntity<Result> createAndEditSupplier(SupplierRequest request) {
        try {
            return ResponseEntity.ok(new Result("SUCCESS", "OK", saveSupplier(request)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Result("NOT FOUND CATEGORY", "NOT_FOUND", null));
        }
    }

    public ResponseEntity<Result> inactive(Long id) {
        try {
            SupplierEntity supplier = supplierRepository.findById(id).orElseThrow(null);
            supplier.setStatus(false);
            supplier.setUpdatedDate(new Date());
            return ResponseEntity.ok(new Result("SUCCESS", "OK", supplierRepository.save(supplier)));
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Result("NOT FOUND CATEGORY", "NOT_FOUND", null));
        }
    }

    public ResponseEntity<Result> active(Long id) {
        try {
            SupplierEntity supplier = supplierRepository.findById(id).orElseThrow(null);
            supplier.setStatus(true);
            supplier.setUpdatedDate(new Date());
            return ResponseEntity.ok(new Result("SUCCESS", "OK", supplierRepository.save(supplier)));
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Result("NOT FOUND CATEGORY", "NOT_FOUND", null));
        }
    }

    public ResponseEntity<Result> delete(Long id) {
        try {
            SupplierEntity supplier = supplierRepository.findById(id).orElseThrow(null);
            supplierRepository.delete(supplier);
            return ResponseEntity.ok(new Result("SUCCESS", "OK", null));
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Result("NOT FOUND CATEGORY", "NOT_FOUND", null));
        }
    }

    public ResponseEntity<Result> search(SearchRequest request) {
        MetaList metaList = request.getMeta();
        Pageable pageable = buildPageable(request.getMeta(), "created_date", true);
        Page<SupplierEntity> page = supplierRepository.search(request.getKeyword(), request.getFromDate(), request.getToDate(), pageable);
        metaList.setTotal(page.getTotalElements());
        SearchResponse<SupplierEntity> response = new SearchResponse<>(page.toList(), metaList);
        return ResponseEntity.ok(new Result("SUCCESS", "OK", response));
    }

    public ResponseEntity<Result> getDetailSupplier(Long id) {
        try {
            SupplierEntity supplier = supplierRepository.findById(id).orElseThrow(null);
            return ResponseEntity.ok(new Result("SUCCESS", "OK", supplier));
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Result("NOT FOUND CATEGORY", "NOT_FOUND", null));
        }
    }

    private SupplierEntity saveSupplier(SupplierRequest request) {
        SupplierEntity supplier = null;
        if (request.getId() == null) {
            supplier = new SupplierEntity();
        } else {
            supplier = supplierRepository.findById(request.getId()).orElse(null);
            if (supplier == null) {
                throw new NullPointerException("Not found supplier!");
            }
            supplier.setUpdatedDate(new Date());
        }
        supplier.setContactName(request.getContactName());
        supplier.setPhone(request.getPhone());
        supplier.setEmail(request.getEmail());
        supplier.setAddress(request.getAddress());
        supplier.setCreatedBy(getNameByToken());
        supplier.setUpdatedBy(getNameByToken());
        return supplierRepository.save(supplier);
    }

}
