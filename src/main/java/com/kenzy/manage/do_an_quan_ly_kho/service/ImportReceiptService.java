package com.kenzy.manage.do_an_quan_ly_kho.service;

import com.kenzy.manage.do_an_quan_ly_kho.entity.ImportReceiptDetailEntity;
import com.kenzy.manage.do_an_quan_ly_kho.entity.ImportReceiptEntity;
import com.kenzy.manage.do_an_quan_ly_kho.entity.ProductEntity;
import com.kenzy.manage.do_an_quan_ly_kho.entity.constant.Result;
import com.kenzy.manage.do_an_quan_ly_kho.model.request.ImportReceiptRequest;
import com.kenzy.manage.do_an_quan_ly_kho.repository.ImportReceiptDetailRepository;
import com.kenzy.manage.do_an_quan_ly_kho.repository.ImportReceiptRepository;
import com.kenzy.manage.do_an_quan_ly_kho.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class ImportReceiptService extends BaseService {
    @Autowired
    private ImportReceiptRepository importReceiptRepository;
    @Autowired
    private ImportReceiptDetailRepository importReceiptDetailRepository;
    @Autowired
    private ProductRepository productRepository;

    public ResponseEntity<Result> createAndUpdate(ImportReceiptRequest request) {
        try {
            return ResponseEntity.ok(new Result("SUCCESS", "OK", createAndEditImportReceipt(request)));
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Result(e.getMessage(), "NOT_FOUND", null));
        }
    }

    private ImportReceiptDetailEntity createAndEditImportReceipt(ImportReceiptRequest request) {
        ImportReceiptEntity importReceipt = null;
        ImportReceiptDetailEntity importReceiptDetail = null;
        if (request.getId() == null) {
            importReceipt = new ImportReceiptEntity();
            importReceiptDetail = new ImportReceiptDetailEntity();
        } else {
            importReceipt = importReceiptRepository.findById(request.getId()).orElseThrow(null);
            if (importReceipt == null) {
                throw new NullPointerException("Not found import receipt");
            }
            importReceiptDetail = importReceiptDetailRepository.findByImportReceiptId(request.getId());
            importReceipt.setUpdatedDate(new Date());
            importReceiptDetail.setUpdatedDate(new Date());
        }
        importReceipt.setImportDate(new Date());
        importReceipt.setName(request.getName());
        importReceiptDetail.setQuantity(request.getQuantity());
        importReceiptDetail.setProductId(request.getProductId());
        ProductEntity product = productRepository.findById(request.getProductId()).orElseThrow(null);
        if (product == null) {
            throw new NullPointerException("Not found product");
        }
        BigDecimal totalPrice = product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity()));
        importReceiptDetail.setTotalPrice(totalPrice);
        importReceiptRepository.save(importReceipt);
        importReceiptDetail.setImportReceiptId(importReceipt.getId());
        return importReceiptDetailRepository.save(importReceiptDetail);
    }
}
