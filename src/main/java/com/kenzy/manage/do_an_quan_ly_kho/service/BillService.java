package com.kenzy.manage.do_an_quan_ly_kho.service;

import com.kenzy.manage.do_an_quan_ly_kho.entity.BillEntity;
import com.kenzy.manage.do_an_quan_ly_kho.entity.PaymentEntity;
import com.kenzy.manage.do_an_quan_ly_kho.entity.constant.Result;
import com.kenzy.manage.do_an_quan_ly_kho.model.request.BillRequest;
import com.kenzy.manage.do_an_quan_ly_kho.model.response.BillResponse;
import com.kenzy.manage.do_an_quan_ly_kho.model.response.PaymentResponse;
import com.kenzy.manage.do_an_quan_ly_kho.repository.BillRepository;
import com.kenzy.manage.do_an_quan_ly_kho.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class BillService extends BaseService {
    @Autowired
    private BillRepository billRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private PaymentService paymentService;

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
            bill.setStatus(false);
            return ResponseEntity.ok(new Result("SUCCESS", "OK", billRepository.save(bill)));
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
        bill.setPaymentId(request.getPaymentId());
        PaymentEntity payment = paymentRepository.findById(request.getId()).orElse(null);
        if (payment == null) {
            throw new NullPointerException("Not found payment");
        }
        bill.setTotalPrice(payment.getPaymentAmount());
        return billRepository.save(bill);
    }
}
