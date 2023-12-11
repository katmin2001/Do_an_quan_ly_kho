package com.kenzy.manage.do_an_quan_ly_kho.service;

import com.kenzy.manage.do_an_quan_ly_kho.entity.BillEntity;
import com.kenzy.manage.do_an_quan_ly_kho.entity.PaymentEntity;
import com.kenzy.manage.do_an_quan_ly_kho.entity.constant.Result;
import com.kenzy.manage.do_an_quan_ly_kho.model.request.BillRequest;
import com.kenzy.manage.do_an_quan_ly_kho.model.request.SearchRequest;
import com.kenzy.manage.do_an_quan_ly_kho.model.response.*;
import com.kenzy.manage.do_an_quan_ly_kho.repository.BillRepository;
import com.kenzy.manage.do_an_quan_ly_kho.repository.PaymentRepository;
import com.kenzy.manage.do_an_quan_ly_kho.repository.ProductRepository;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
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
public class BillService extends BaseService {
    @Autowired
    private BillRepository billRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderService orderService;

    public ResponseEntity<Result> createAndEdit(BillRequest request) {
        try {
            return ResponseEntity.ok(new Result("SUCCESS", "OK", saveBill(request)));
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Result(e.getMessage(), "NOT_FOUND", null));
        }
    }

    public ResponseEntity<Result> delete(Long id) {
        try {
            BillEntity bill = billRepository.findById(id).orElse(null);
            if (bill == null) {
                throw new NullPointerException("Not found bill");
            }
            billRepository.delete(bill);
            return ResponseEntity.ok(new Result("SUCCESS", "OK", null));
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Result(e.getMessage(), "NOT_FOUND", null));
        }
    }

    public ResponseEntity<Result> detailBill(Long id) {
        try {
            return ResponseEntity.ok(new Result("SUCCESS", "OK", detail(id)));
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Result(e.getMessage(), "NOT_FOUND", null));
        }
    }

    public ResponseEntity<Result> searchBill(SearchRequest request) {
        MetaList metaList = request.getMeta();
        Pageable pageable = buildPageable(request.getMeta(), "created_date", true);
        Page<BillEntity> page = billRepository.search(request.getFromDate(), request.getToDate(), pageable);
        List<BillResponse> responses = new ArrayList<>();
        for (BillEntity bill : page) {
            responses.add(detail(bill.getId()));
        }
        metaList.setTotal(page.getTotalElements());
        SearchResponse<BillResponse> response = new SearchResponse<>(responses, metaList);
        return ResponseEntity.ok(new Result("SUCCESS", "OK", response));
    }

    private BillResponse detail(Long id) {
        BillEntity bill = billRepository.findById(id).orElse(null);
        if (bill == null) {
            throw new NullPointerException("Not found bill");
        }
        BillResponse response = new BillResponse();
        response.setId(id);
        response.setCreatedDate(bill.getCreatedDate());
        response.setCreatedBy(bill.getCreatedBy());
        response.setPaymentResponse(paymentService.detail(bill.getPaymentId()));
        PaymentEntity payment = paymentRepository.findById(bill.getPaymentId()).orElse(null);
        OrderResponse orderResponse = orderService.detail(payment.getOrderId());
        response.setOrderResponse(orderResponse);
        return response;
    }

    private BillEntity saveBill(BillRequest request) {
        BillEntity bill = null;
        if (request.getId() == null) {
            bill = new BillEntity();
        } else {
            bill = billRepository.findById(request.getId()).orElse(null);
            if (bill == null) {
                throw new NullPointerException("Not found bill");
            }
            bill.setUpdatedDate(new Date());
        }
        PaymentEntity payment = paymentRepository.findById(request.getPaymentId()).orElse(null);
        if (payment == null) {
            throw new NullPointerException("Not found payment");
        }
        bill.setPaymentId(request.getPaymentId());
        bill.setTotalPrice(payment.getPaymentAmount());
        bill.setCreatedBy(getNameByToken());
        bill.setUpdatedBy(getNameByToken());
        return billRepository.save(bill);
    }
}
