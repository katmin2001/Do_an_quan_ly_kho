package com.kenzy.manage.do_an_quan_ly_kho.service;

import com.kenzy.manage.do_an_quan_ly_kho.entity.ImportReceiptDetailEntity;
import com.kenzy.manage.do_an_quan_ly_kho.entity.ImportReceiptEntity;
import com.kenzy.manage.do_an_quan_ly_kho.entity.ProductEntity;
import com.kenzy.manage.do_an_quan_ly_kho.entity.constant.Result;
import com.kenzy.manage.do_an_quan_ly_kho.model.request.ImportReceiptDetailRequest;
import com.kenzy.manage.do_an_quan_ly_kho.model.request.ImportReceiptRequest;
import com.kenzy.manage.do_an_quan_ly_kho.repository.ImportReceiptDetailRepository;
import com.kenzy.manage.do_an_quan_ly_kho.repository.ImportReceiptRepository;
import com.kenzy.manage.do_an_quan_ly_kho.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    private List<ImportReceiptDetailEntity> createAndEditImportReceipt(ImportReceiptRequest request) {
        ImportReceiptEntity importReceipt = null;
        List<ImportReceiptDetailEntity> importReceiptDetailList = new ArrayList<>();
        if (request.getId() == null) {
            importReceipt = new ImportReceiptEntity();
        } else {
            importReceipt = importReceiptRepository.findById(request.getId()).orElseThrow(null);
            if (importReceipt == null) {
                throw new NullPointerException("Not found import receipt");
            }
            importReceiptDetailList = importReceiptDetailRepository.getImportReceiptDetailEntitiesByImportReceiptId(request.getId());
            importReceiptDetailRepository.deleteAll(importReceiptDetailList);
            importReceiptDetailList.clear();
            importReceipt.setUpdatedDate(new Date());
        }
        importReceipt.setImportDate(new Date());
        importReceipt.setName(request.getName());
        importReceiptRepository.save(importReceipt);
        for(ImportReceiptDetailRequest receiptDetailRequest: request.getImportReceiptDetailRequest()){
            ImportReceiptDetailEntity importReceiptDetail = new ImportReceiptDetailEntity();
            importReceiptDetail.setQuantity(receiptDetailRequest.getQuantity());
            importReceiptDetail.setProductId(receiptDetailRequest.getProductId());
            ProductEntity product = productRepository.findById(receiptDetailRequest.getProductId()).orElseThrow(null);
            if (product == null) {
                throw new NullPointerException("Not found product");
            }
            BigDecimal totalPrice = product.getPrice().multiply(BigDecimal.valueOf(receiptDetailRequest.getQuantity()));
            importReceiptDetail.setTotalPrice(totalPrice);
            importReceiptDetail.setImportReceiptId(importReceipt.getId());
            importReceiptDetail.setUpdatedDate(new Date());
            importReceiptDetailList.add(importReceiptDetail);
        }
        return importReceiptDetailRepository.saveAll(importReceiptDetailList);
    }
}
