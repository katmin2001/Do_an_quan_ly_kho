package com.kenzy.manage.do_an_quan_ly_kho.service;

import com.kenzy.manage.do_an_quan_ly_kho.entity.ExportReceiptDetailEntity;
import com.kenzy.manage.do_an_quan_ly_kho.entity.ExportReceiptEntity;
import com.kenzy.manage.do_an_quan_ly_kho.entity.OrderDetailEntity;
import com.kenzy.manage.do_an_quan_ly_kho.entity.ProductEntity;
import com.kenzy.manage.do_an_quan_ly_kho.entity.constant.Result;
import com.kenzy.manage.do_an_quan_ly_kho.model.request.ExportReceiptRequest;
import com.kenzy.manage.do_an_quan_ly_kho.model.request.SearchRequest;
import com.kenzy.manage.do_an_quan_ly_kho.model.response.ExportReceiptDetailResponse;
import com.kenzy.manage.do_an_quan_ly_kho.model.response.ExportReceiptResponse;
import com.kenzy.manage.do_an_quan_ly_kho.model.response.MetaList;
import com.kenzy.manage.do_an_quan_ly_kho.model.response.SearchResponse;
import com.kenzy.manage.do_an_quan_ly_kho.repository.*;
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
public class ExportReceiptService extends BaseService {
    @Autowired
    private ExportReceiptRepository exportReceiptRepository;
    @Autowired
    private ExportReceiptDetailRepository exportReceiptDetailRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ImportReceiptDetailRepository importReceiptDetailRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private OrderRepository orderRepository;

    public ResponseEntity<Result> createAndEdit(ExportReceiptRequest request) {
        try {
            return ResponseEntity.ok(new Result("SUCCESS", "OK", saveExportReceipt(request)));
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Result(e.getMessage(), "NOT_FOUND", null));
        }
    }

    public ResponseEntity<Result> delete(Long id) {
        try {
            return ResponseEntity.ok(new Result("SUCCESS", "OK", deleteExportReceipt(id)));
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Result(e.getMessage(), "NOT_FOUND", null));
        }
    }

    public ResponseEntity<Result> restore(Long id) {
        try {
            return ResponseEntity.ok(new Result("SUCCESS", "OK", restoreExportReceipt(id)));
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Result(e.getMessage(), "NOT_FOUND", null));
        }
    }

    public ResponseEntity<Result> detail(Long id) {
        try {
            return ResponseEntity.ok(new Result("SUCCESS", "OK", detailExportReceipt(id)));
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Result(e.getMessage(), "NOT_FOUND", null));
        }
    }

    private List<ExportReceiptDetailResponse> detailExportReceipt(Long id) {
        ExportReceiptEntity exportReceipt = exportReceiptRepository.findById(id).orElse(null);
        if (exportReceipt == null) {
            throw new NullPointerException("Not found export receipt");
        }
        List<ExportReceiptDetailResponse> exportReceiptDetailResponseList = new ArrayList<>();
        List<ExportReceiptDetailEntity> exportReceiptDetailEntityList = exportReceiptDetailRepository.findExportReceiptDetailEntitiesByExportReceiptId(id);
        for (ExportReceiptDetailEntity exportReceiptDetail : exportReceiptDetailEntityList) {
            ExportReceiptDetailResponse response = mapperExportReceiptDetail(exportReceiptDetail);
            exportReceiptDetailResponseList.add(response);
        }
        return exportReceiptDetailResponseList;
    }

    public ResponseEntity<Result> search(SearchRequest request) {
        MetaList metaList = request.getMeta();
        Pageable pageable = buildPageable(request.getMeta(), "created_date", true);
        Page<ExportReceiptEntity> page = exportReceiptRepository.search(request.getKeyword(), request.getFromDate(), request.getToDate(), pageable);
        List<ExportReceiptResponse> responses = new ArrayList<>();
        for (ExportReceiptEntity exportReceipt : page) {
            ExportReceiptResponse response = mapperExportReceipt(exportReceipt);
            responses.add(response);
        }
        metaList.setTotal(page.getTotalElements());
        SearchResponse<ExportReceiptResponse> response = new SearchResponse<>(responses, metaList);
        return ResponseEntity.ok(new Result("SUCCESS", "OK", response));
    }

    private ExportReceiptResponse mapperExportReceipt(ExportReceiptEntity exportReceipt) {
        ExportReceiptResponse response = new ExportReceiptResponse();
        response.setId(exportReceipt.getId());
        response.setName(exportReceipt.getName());
        response.setExportDate(exportReceipt.getExportDate());
        response.setCreatedBy(exportReceipt.getCreatedBy());
        response.setCreatedDate(exportReceipt.getCreatedDate());
        response.setStatus(exportReceipt.getStatus());
        response.setTotalPrice(totalPrice(exportReceipt.getId()));
        response.setOrderId(exportReceipt.getOrderId());
        List<ExportReceiptDetailResponse> exportReceiptDetailResponseList = new ArrayList<>();
        List<ExportReceiptDetailEntity> exportReceiptDetailEntityList = exportReceiptDetailRepository.findExportReceiptDetailEntitiesByExportReceiptId(exportReceipt.getId());
        for (ExportReceiptDetailEntity exportReceiptDetail : exportReceiptDetailEntityList) {
            exportReceiptDetailResponseList.add(mapperExportReceiptDetail(exportReceiptDetail));
        }
        response.setExportReceiptDetailResponseList(exportReceiptDetailResponseList);
        return response;
    }

    private BigDecimal totalPrice(Long id) {
        BigDecimal totalPrice = BigDecimal.valueOf(0);
        List<ExportReceiptDetailEntity> exportReceiptDetailEntityList = exportReceiptDetailRepository.findExportReceiptDetailEntitiesByExportReceiptId(id);
        for (ExportReceiptDetailEntity exportReceiptDetail : exportReceiptDetailEntityList) {
            totalPrice = totalPrice.add(exportReceiptDetail.getTotalPrice());
        }
        return totalPrice;
    }

    private ExportReceiptDetailResponse mapperExportReceiptDetail(ExportReceiptDetailEntity exportReceiptDetail) {
        ExportReceiptDetailResponse response = new ExportReceiptDetailResponse();
        response.setId(exportReceiptDetail.getId());
        response.setQuantity(exportReceiptDetail.getQuantity());
        response.setUnitPrice(exportReceiptDetail.getUnitPrice());
        response.setTotalPriceProduct(exportReceiptDetail.getTotalPrice());
        ProductEntity product = productRepository.findById(exportReceiptDetail.getProductId()).orElse(null);
        if (product == null) {
            throw new NullPointerException("Not found product");
        }
        response.setProductName(product.getProductName());
        response.setImageUrls(List.of(product.getProductImages()));
        return response;
    }

    private ExportReceiptEntity deleteExportReceipt(Long id) {
        ExportReceiptEntity exportReceipt = exportReceiptRepository.findById(id).orElse(null);
        if (exportReceipt == null) {
            throw new NullPointerException("Not found export receipt");
        }
        exportReceipt.setStatus(false);
        List<ExportReceiptDetailEntity> exportReceiptDetailEntityList = exportReceiptDetailRepository.findExportReceiptDetailEntitiesByExportReceiptId(id);
        for (ExportReceiptDetailEntity exportReceiptDetail : exportReceiptDetailEntityList) {
            exportReceiptDetail.setStatus(false);
        }
        exportReceiptDetailRepository.saveAll(exportReceiptDetailEntityList);
        return exportReceiptRepository.save(exportReceipt);
    }

    private ExportReceiptEntity restoreExportReceipt(Long id) {
        ExportReceiptEntity exportReceipt = exportReceiptRepository.findById(id).orElse(null);
        if (exportReceipt == null) {
            throw new NullPointerException("Not found export receipt");
        }
        exportReceipt.setStatus(true);
        List<ExportReceiptDetailEntity> exportReceiptDetailEntityList = exportReceiptDetailRepository.findExportReceiptDetailEntitiesByExportReceiptId(id);
        for (ExportReceiptDetailEntity exportReceiptDetail : exportReceiptDetailEntityList) {
            exportReceiptDetail.setStatus(true);
        }
        exportReceiptDetailRepository.saveAll(exportReceiptDetailEntityList);
        return exportReceiptRepository.save(exportReceipt);
    }

    private List<ExportReceiptDetailEntity> saveExportReceipt(ExportReceiptRequest request) {
        ExportReceiptEntity exportReceipt = null;
        List<ExportReceiptDetailEntity> exportReceiptDetailEntityList = new ArrayList<>();
        if (request.getId() == null) {
            exportReceipt = new ExportReceiptEntity();
        } else {
            exportReceipt = exportReceiptRepository.findById(request.getId()).orElse(null);
            if (exportReceipt == null) {
                throw new NullPointerException("Not found export receipt");
            }
            exportReceiptDetailEntityList = exportReceiptDetailRepository.findExportReceiptDetailEntitiesByExportReceiptId(exportReceipt.getId());
            exportReceiptDetailRepository.deleteAll(exportReceiptDetailEntityList);
            exportReceiptDetailEntityList.clear();
            exportReceipt.setUpdatedDate(new Date());
        }
        exportReceipt.setExportDate(request.getExportDate());
        exportReceipt.setName(request.getName());
        exportReceipt.setOrderId(request.getOrderId());
        exportReceipt.setCreatedBy(getNameByToken());
        exportReceipt.setUpdatedBy(getNameByToken());
        exportReceiptRepository.save(exportReceipt);
        List<OrderDetailEntity> orderDetailEntityList = orderDetailRepository.findOrderDetailEntitiesByOrderId(request.getOrderId());
        for (OrderDetailEntity orderDetail : orderDetailEntityList) {
            ExportReceiptDetailEntity exportReceiptDetail = new ExportReceiptDetailEntity();
            Integer quantityInWareHouse = importReceiptDetailRepository.getQuantityProductInWareHouseById(orderDetail.getProductId());
            if (quantityInWareHouse == null) {
                quantityInWareHouse = 0;
            }
            Integer quantityExport = exportReceiptDetailRepository.getQuantityProductExportById(orderDetail.getProductId());
            if (quantityExport == null) {
                quantityExport = 0;
            }
            ProductEntity product = productRepository.findById(orderDetail.getProductId()).orElse(null);
            if (product == null) {
                throw new NullPointerException("Not found product");
            }
            if ((quantityInWareHouse - quantityExport) < orderDetail.getQuantity()) {
                throw new RuntimeException("Not enough product: " + product.getProductName());
            }
            exportReceiptDetail.setExportReceiptId(exportReceipt.getId());
            exportReceiptDetail.setProductId(orderDetail.getProductId());
            exportReceiptDetail.setQuantity(orderDetail.getQuantity());
            exportReceiptDetail.setCreatedDate(new Date());
            exportReceiptDetail.setUnitPrice(product.getPrice());
            exportReceiptDetail.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(orderDetail.getQuantity())));
            exportReceiptDetail.setUpdatedBy(getNameByToken());
            exportReceiptDetail.setCreatedBy(getNameByToken());
            exportReceiptDetailEntityList.add(exportReceiptDetail);
        }
        return exportReceiptDetailRepository.saveAll(exportReceiptDetailEntityList);
    }

//    private BigDecimal exportPrice(Long productId, int quantityExport) {
//        List<ImportReceiptDetailEntity> importReceiptDetailEntityList = importReceiptDetailRepository.getImportReceiptDetailEntitiesByProductId(productId);
//        List<ExportReceiptDetailEntity> exportReceiptDetailEntityList = exportReceiptDetailRepository.getExportReceiptDetailEntitiesByProductId(productId);
//        BigDecimal priceImportTotal = BigDecimal.valueOf(0);
//        for (ImportReceiptDetailEntity importReceiptDetail : importReceiptDetailEntityList) {
//            if (quantityExport > importReceiptDetail.getQuantity()) {
//                quantityExport -= importReceiptDetail.getQuantity();
//                priceImportTotal = priceImportTotal.add(importReceiptDetail.getUnitPrice().multiply(BigDecimal.valueOf(importReceiptDetail.getQuantity())));
//            } else {
//                priceImportTotal = priceImportTotal.add(importReceiptDetail.getUnitPrice().multiply(BigDecimal.valueOf(quantityExport)));
//            }
//            priceImportTotal = priceImportTotal.add(importReceiptDetail.getTotalPrice());
//        }
//        return priceImportTotal.divide(BigDecimal.valueOf(quantityExport));
//    }
//
//    private void setImportReceiptStatus(Long importRecepit) {
//
//    }
}
