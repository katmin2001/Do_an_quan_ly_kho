package com.kenzy.manage.do_an_quan_ly_kho.service;

import com.kenzy.manage.do_an_quan_ly_kho.entity.ImportReceiptDetailEntity;
import com.kenzy.manage.do_an_quan_ly_kho.entity.ImportReceiptEntity;
import com.kenzy.manage.do_an_quan_ly_kho.entity.ProductEntity;
import com.kenzy.manage.do_an_quan_ly_kho.entity.constant.Result;
import com.kenzy.manage.do_an_quan_ly_kho.model.request.ImportReceiptDetailRequest;
import com.kenzy.manage.do_an_quan_ly_kho.model.request.ImportReceiptRequest;
import com.kenzy.manage.do_an_quan_ly_kho.model.request.SearchRequest;
import com.kenzy.manage.do_an_quan_ly_kho.model.response.*;
import com.kenzy.manage.do_an_quan_ly_kho.repository.ImportReceiptDetailRepository;
import com.kenzy.manage.do_an_quan_ly_kho.repository.ImportReceiptRepository;
import com.kenzy.manage.do_an_quan_ly_kho.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public ResponseEntity<Result> deleteImportReceipt(Long id) {
        try {
            delete(id);
            return ResponseEntity.ok(new Result("SUCCESS", "OK", null));
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Result("Not found import receipt", "NOT_FOUND", null));
        }
    }

    public ResponseEntity<Result> detailImportReceipt(Long id) {
        try {
            return ResponseEntity.ok(new Result("SUCCESS", "OK", detail(id)));
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Result(e.getMessage(), "NOT_FOUND", null));
        }
    }

    public ResponseEntity<Result> search(SearchRequest request){
        MetaList metaList = request.getMeta();
        Pageable pageable = buildPageable(request.getMeta(), "created_date", true);
        Page<ImportReceiptEntity> page = importReceiptRepository.search(request.getKeyword(), request.getFromDate(), request.getToDate(), pageable);
        List<ImportReceiptResponse> responses = new ArrayList<>();
        for(ImportReceiptEntity importReceipt: page){
            List<ImportReceiptDetailEntity> importReceiptDetailList = importReceiptDetailRepository.getImportReceiptDetailEntitiesByImportReceiptId(importReceipt.getId());
            ImportReceiptResponse receiptResponse = new ImportReceiptResponse();
            receiptResponse.setImportDate(importReceipt.getImportDate());
            receiptResponse.setId(importReceipt.getId());
            receiptResponse.setName(importReceipt.getName());
            List<ImportReceiptDetailResponse> importReceiptDetailResponseList = new ArrayList<>();
            BigDecimal totalPrice = BigDecimal.valueOf(0);
            for (ImportReceiptDetailEntity receiptDetail : importReceiptDetailList) {
                ImportReceiptDetailResponse detailResponse = new ImportReceiptDetailResponse();
                ProductEntity product = productRepository.findById(receiptDetail.getProductId()).orElseThrow(null);
                if (product == null) {
                    throw new NullPointerException("Not found product");
                }
                detailResponse.setProductName(product.getProductName());
                detailResponse.setPriceUnit(product.getPrice());
                detailResponse.setId(receiptDetail.getId());
                detailResponse.setQuantity(receiptDetail.getQuantity());
                detailResponse.setTotalPriceProduct(product.getPrice().multiply(BigDecimal.valueOf(receiptDetail.getQuantity())));
                totalPrice = totalPrice.add(detailResponse.getTotalPriceProduct());
                importReceiptDetailResponseList.add(detailResponse);
            }
            receiptResponse.setTotalPrice(totalPrice);
            receiptResponse.setImportReceiptDetailResponseList(importReceiptDetailResponseList);
            responses.add(receiptResponse);
        }
        metaList.setTotal(page.getTotalElements());
        SearchResponse<ImportReceiptResponse> response = new SearchResponse<>(responses, metaList);
        return ResponseEntity.ok(new Result("SUCCESS", "OK", response));
    }

    private void delete(Long id) {
        ImportReceiptEntity importReceipt = importReceiptRepository.findById(id).orElseThrow(null);
        List<ImportReceiptDetailEntity> importReceiptDetailList = importReceiptDetailRepository.getImportReceiptDetailEntitiesByImportReceiptId(id);
        importReceiptDetailRepository.deleteAll(importReceiptDetailList);
        importReceiptRepository.delete(importReceipt);
    }

    private ImportReceiptResponse detail(Long id) {
        ImportReceiptEntity importReceipt = importReceiptRepository.findById(id).orElse(null);
        if (importReceipt == null) {
            throw new NullPointerException("Not found import receipt");
        }
        List<ImportReceiptDetailEntity> importReceiptDetailList = importReceiptDetailRepository.getImportReceiptDetailEntitiesByImportReceiptId(id);
        ImportReceiptResponse response = new ImportReceiptResponse();
        response.setImportDate(importReceipt.getImportDate());
        response.setId(importReceipt.getId());
        response.setName(importReceipt.getName());
        List<ImportReceiptDetailResponse> importReceiptDetailResponseList = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.valueOf(0);
        for (ImportReceiptDetailEntity receiptDetail : importReceiptDetailList) {
            ImportReceiptDetailResponse detailResponse = new ImportReceiptDetailResponse();
            ProductEntity product = productRepository.findById(receiptDetail.getProductId()).orElseThrow(null);
            if (product == null) {
                throw new NullPointerException("Not found product");
            }
            detailResponse.setProductName(product.getProductName());
            detailResponse.setPriceUnit(product.getPrice());
            detailResponse.setId(receiptDetail.getId());
            detailResponse.setQuantity(receiptDetail.getQuantity());
            detailResponse.setTotalPriceProduct(product.getPrice().multiply(BigDecimal.valueOf(receiptDetail.getQuantity())));
            totalPrice = totalPrice.add(detailResponse.getTotalPriceProduct());
            importReceiptDetailResponseList.add(detailResponse);
        }
        response.setTotalPrice(totalPrice);
        response.setImportReceiptDetailResponseList(importReceiptDetailResponseList);
        return response;
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
        for (ImportReceiptDetailRequest receiptDetailRequest : request.getImportReceiptDetailRequest()) {
            ImportReceiptDetailEntity importReceiptDetail = new ImportReceiptDetailEntity();
            importReceiptDetail.setQuantity(receiptDetailRequest.getQuantity());
            importReceiptDetail.setProductId(receiptDetailRequest.getProductId());
//            ProductEntity product = productRepository.findById(receiptDetailRequest.getProductId()).orElseThrow(null);
//            if (product == null) {
//                throw new NullPointerException("Not found product");
//            }
            importReceiptDetail.setUnitPrice(receiptDetailRequest.getPriceUnit());
            BigDecimal totalPrice = receiptDetailRequest.getPriceUnit().multiply(BigDecimal.valueOf(receiptDetailRequest.getQuantity()));
            importReceiptDetail.setTotalPrice(totalPrice);
            importReceiptDetail.setImportReceiptId(importReceipt.getId());
            importReceiptDetail.setUpdatedDate(new Date());
            importReceiptDetailList.add(importReceiptDetail);
        }
        return importReceiptDetailRepository.saveAll(importReceiptDetailList);
    }
}
