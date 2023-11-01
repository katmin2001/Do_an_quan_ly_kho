package com.kenzy.manage.do_an_quan_ly_kho.service;

import com.kenzy.manage.do_an_quan_ly_kho.entity.CustomerEntity;
import com.kenzy.manage.do_an_quan_ly_kho.entity.OrderEntity;
import com.kenzy.manage.do_an_quan_ly_kho.entity.PaymentEntity;
import com.kenzy.manage.do_an_quan_ly_kho.entity.constant.Result;
import com.kenzy.manage.do_an_quan_ly_kho.model.request.PaymentRequest;
import com.kenzy.manage.do_an_quan_ly_kho.model.request.SearchRequest;
import com.kenzy.manage.do_an_quan_ly_kho.model.response.MetaList;
import com.kenzy.manage.do_an_quan_ly_kho.model.response.OrderPaymentResponse;
import com.kenzy.manage.do_an_quan_ly_kho.model.response.PaymentResponse;
import com.kenzy.manage.do_an_quan_ly_kho.model.response.SearchResponse;
import com.kenzy.manage.do_an_quan_ly_kho.repository.CustomerRepository;
import com.kenzy.manage.do_an_quan_ly_kho.repository.OrderRepository;
import com.kenzy.manage.do_an_quan_ly_kho.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PaymentService extends BaseService {
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CustomerRepository customerRepository;

    public ResponseEntity<Result> createAndEdit(PaymentRequest request) {
        try {
            return ResponseEntity.ok(new Result("SUCCESS", "OK", savePayment(request)));
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Result(e.getMessage(), "NOT_FOUND", null));
        }
    }

    public ResponseEntity<Result> detailPayment(Long id) {
        try {
            return ResponseEntity.ok(new Result("SUCCESS", "OK", detail(id)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Result(e.getMessage(), "NOT_FOUND", null));
        }
    }

    public ResponseEntity<Result> deletePayment(Long id) {
        try {
            return ResponseEntity.ok(new Result("SUCCESS", "OK", delete(id)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Result(e.getMessage(), "NOT_FOUND", null));
        }
    }

    public ResponseEntity<Result> searchPayment(SearchRequest request) {
        MetaList metaList = request.getMeta();
        Pageable pageable = buildPageable(request.getMeta(), "created_date", true);
        Page<PaymentEntity> page = paymentRepository.search(request.getFromDate(), request.getToDate(), pageable);
        List<PaymentResponse> responses = new ArrayList<>();
        for (PaymentEntity payment : page) {
            responses.add(detail(payment.getId()));
        }
        metaList.setTotal(page.getTotalElements());
        SearchResponse<PaymentResponse> response = new SearchResponse<>(responses, metaList);
        return ResponseEntity.ok(new Result("SUCCESS", "OK", response));
    }

    private PaymentEntity delete(Long id) {
        PaymentEntity payment = paymentRepository.findById(id).orElse(null);
        if (payment == null) {
            throw new NullPointerException("Not found payment");
        }
        payment.setStatus(false);
        return paymentRepository.save(payment);
    }

    private PaymentEntity savePayment(PaymentRequest request) {
        PaymentEntity payment = null;
        if (request.getId() == null) {
            payment = new PaymentEntity();
        } else {
            payment = paymentRepository.findById(request.getId()).orElse(null);
            if (payment == null) {
                throw new NullPointerException("Not found payment");
            }
            payment.setUpdatedDate(new Date());
        }
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setOrderId(request.getOrderId());
        payment.setPaymentDate(new Date());
        OrderEntity order = orderRepository.findById(request.getOrderId()).orElse(null);
        if (order == null) {
            throw new NullPointerException("Not found order");
        }
        payment.setPaymentAmount(order.getTotalAmount());
        return paymentRepository.save(payment);
    }

    public PaymentResponse detail(Long id) {
        PaymentEntity payment = paymentRepository.findById(id).orElse(null);
        if (payment == null) {
            throw new NullPointerException("Not found payment");
        }
        PaymentResponse response = new PaymentResponse();
        response.setId(payment.getId());
        response.setPaymentMethod(payment.getPaymentMethod());
        response.setPaymentDate(payment.getPaymentDate());
        response.setPaymentAmount(payment.getPaymentAmount());
        OrderEntity order = orderRepository.findById(payment.getOrderId()).orElse(null);
        if (order == null) {
            throw new NullPointerException("Not found order");
        }
        response.setOrderPaymentResponse(mapperOrderPayment(order));
        return response;
    }

    private OrderPaymentResponse mapperOrderPayment(OrderEntity order) {
        OrderPaymentResponse response = new OrderPaymentResponse();
        response.setId(order.getId());
        response.setOrderDate(order.getOrderDate());
        CustomerEntity customer = customerRepository.findById(order.getCustomerId()).orElse(null);
        if (customer == null) {
            throw new NullPointerException("Not found customer");
        }
        response.setNameCustomer(customer.getName());
        response.setOrderStatus(order.getOrderStatus());
        return response;
    }
}
