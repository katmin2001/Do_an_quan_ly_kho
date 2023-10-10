package com.kenzy.manage.do_an_quan_ly_kho.service;

import com.kenzy.manage.do_an_quan_ly_kho.entity.CustomerEntity;
import com.kenzy.manage.do_an_quan_ly_kho.entity.constant.Result;
import com.kenzy.manage.do_an_quan_ly_kho.model.request.CustomerRequest;
import com.kenzy.manage.do_an_quan_ly_kho.model.request.SearchRequest;
import com.kenzy.manage.do_an_quan_ly_kho.model.response.MetaList;
import com.kenzy.manage.do_an_quan_ly_kho.model.response.SearchResponse;
import com.kenzy.manage.do_an_quan_ly_kho.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CustomerService extends BaseService {
    @Autowired
    private CustomerRepository customerRepository;

    public ResponseEntity<Result> createAndEditCustomer(CustomerRequest request) {
        try {
            return ResponseEntity.ok(new Result("SUCCESS", "OK", saveCustomer(request)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Result("NOT FOUND CUSTOMER", "NOT_FOUND", null));
        }
    }

    public ResponseEntity<Result> delete(Long id) {
        try {
            CustomerEntity customer = customerRepository.findById(id).orElseThrow(null);
            customer.setStatus(false);
            customer.setUpdatedDate(new Date());
            return ResponseEntity.ok(new Result("SUCCESS", "OK", customerRepository.save(customer)));
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Result("NOT FOUND CUSTOMER", "NOT_FOUND", null));
        }
    }

    public ResponseEntity<Result> active(Long id) {
        try {
            CustomerEntity customer = customerRepository.findById(id).orElseThrow(null);
            customer.setStatus(true);
            customer.setUpdatedDate(new Date());
            return ResponseEntity.ok(new Result("SUCCESS", "OK", customerRepository.save(customer)));
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Result("NOT FOUND CUSTOMER", "NOT_FOUND", null));
        }
    }

    public ResponseEntity<Result> search(SearchRequest request) {
        MetaList metaList = request.getMeta();
        Pageable pageable = buildPageable(request.getMeta(), "created_date", true);
        Page<CustomerEntity> page = customerRepository.search(request.getKeyword(), request.getFromDate(), request.getToDate(), pageable);
        metaList.setTotal(page.getTotalElements());
        SearchResponse<CustomerEntity> response = new SearchResponse<>(page.toList(), metaList);
        return ResponseEntity.ok(new Result("SUCCESS", "OK", response));
    }

    public ResponseEntity<Result> getDetailCustomer(Long id) {
        try {
            CustomerEntity customer = customerRepository.findById(id).orElseThrow(null);
            return ResponseEntity.ok(new Result("SUCCESS", "OK", customer));
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Result("NOT FOUND CATEGORY", "NOT_FOUND", null));
        }
    }

    private CustomerEntity saveCustomer(CustomerRequest request) {
        CustomerEntity customer = null;
        if (request.getId() == null) {
            customer = new CustomerEntity();
        } else {
            customer = customerRepository.findById(request.getId()).orElse(null);
            if (customer == null) {
                throw new NullPointerException("Not found customer!");
            }
            customer.setUpdatedDate(new Date());
        }
        customer.setName(request.getName());
        customer.setPhone(request.getPhone());
        customer.setEmail(request.getEmail());
        customer.setAddress(request.getAddress());
        return customerRepository.save(customer);

    }
}
