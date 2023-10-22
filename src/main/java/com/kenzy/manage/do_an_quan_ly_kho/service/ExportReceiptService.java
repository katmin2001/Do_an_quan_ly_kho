package com.kenzy.manage.do_an_quan_ly_kho.service;

import com.kenzy.manage.do_an_quan_ly_kho.entity.ExportReceiptDetailEntity;
import com.kenzy.manage.do_an_quan_ly_kho.entity.ExportReceiptEntity;
import com.kenzy.manage.do_an_quan_ly_kho.entity.ProductEntity;
import com.kenzy.manage.do_an_quan_ly_kho.entity.constant.Result;
import com.kenzy.manage.do_an_quan_ly_kho.model.request.ExportReceiptRequest;
import com.kenzy.manage.do_an_quan_ly_kho.model.request.OrderProductRequest;
import com.kenzy.manage.do_an_quan_ly_kho.model.request.OrderRequest;
import com.kenzy.manage.do_an_quan_ly_kho.repository.ExportReceiptDetailRepository;
import com.kenzy.manage.do_an_quan_ly_kho.repository.ExportReceiptRepository;
import com.kenzy.manage.do_an_quan_ly_kho.repository.ImportReceiptDetailRepository;
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
public class ExportReceiptService extends BaseService {
    @Autowired
    private ExportReceiptRepository exportReceiptRepository;
    @Autowired
    private ExportReceiptDetailRepository exportReceiptDetailRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ImportReceiptDetailRepository importReceiptDetailRepository;

//    public ResponseEntity<Result> createAndUpdate(OrderRequest request) {
//        try {
//            return ResponseEntity.ok(new Result("SUCCESS", "OK", createAndUpdateOrder(request)));
//        } catch (NullPointerException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Result(e.getMessage(), "NOT_FOUND", null));
//        }
//    }

    public void saveExportReceipt(OrderRequest request, Long orderId) {
        ExportReceiptEntity exportReceipt = null;
        List<ExportReceiptDetailEntity> exportReceiptDetailEntityList = new ArrayList<>();
        if (request.getExportReceiptRequest().getId() == null) {
            exportReceipt = new ExportReceiptEntity();
        } else {
            exportReceipt = exportReceiptRepository.findById(request.getExportReceiptRequest().getId()).orElse(null);
            if (exportReceipt == null) {
                throw new NullPointerException("Not found Receipt");
            }
            exportReceiptDetailEntityList = exportReceiptDetailRepository.findExportReceiptDetailEntitiesByExportReceiptId(request.getExportReceiptRequest().getId());
            exportReceiptDetailRepository.deleteAll(exportReceiptDetailEntityList);
            exportReceiptDetailEntityList.clear();
            exportReceipt.setUpdatedDate(new Date());
        }
        exportReceipt.setExportDate(new Date());
        exportReceipt.setName("Xuất kho đơn: " + orderId);
        exportReceiptRepository.save(exportReceipt);
        for (OrderProductRequest productRequest : request.getOrderProductRequestList()) {
            ExportReceiptDetailEntity exportReceiptDetail = new ExportReceiptDetailEntity();
            long quantityInWareHouse = importReceiptDetailRepository.getQuantityProductInWareHouseById(productRequest.getProductId());
            long quantityExport = exportReceiptDetailRepository.getQuantityProductExportById(productRequest.getProductId());
            ProductEntity product = productRepository.findById(productRequest.getProductId()).orElse(null);
            if (product == null) {
                throw new NullPointerException("Not found product");
            }
            if ((quantityInWareHouse - quantityExport) < productRequest.getQuantity()) {
                throw new RuntimeException("Not enough product: " + product.getProductName());
            }
            exportReceiptDetail.setProductId(productRequest.getProductId());
            exportReceiptDetail.setExportReceiptId(exportReceipt.getId());
            exportReceiptDetail.setQuantity((long) productRequest.getQuantity());
            exportReceiptDetail.setOrderId(orderId);
            exportReceiptDetail.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(productRequest.getQuantity())));
            exportReceiptDetailEntityList.add(exportReceiptDetail);
        }
        exportReceiptDetailRepository.saveAll(exportReceiptDetailEntityList);
    }
}
