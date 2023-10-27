package com.kenzy.manage.do_an_quan_ly_kho.service;

import com.kenzy.manage.do_an_quan_ly_kho.entity.OrderDetailEntity;
import com.kenzy.manage.do_an_quan_ly_kho.entity.OrderEntity;
import com.kenzy.manage.do_an_quan_ly_kho.entity.ProductEntity;
import com.kenzy.manage.do_an_quan_ly_kho.entity.constant.OrderStatus;
import com.kenzy.manage.do_an_quan_ly_kho.entity.constant.Result;
import com.kenzy.manage.do_an_quan_ly_kho.model.request.OrderProductRequest;
import com.kenzy.manage.do_an_quan_ly_kho.model.request.OrderRequest;
import com.kenzy.manage.do_an_quan_ly_kho.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderService extends BaseService {
    @Autowired
    private ExportReceiptRepository exportReceiptRepository;
    @Autowired
    private ExportReceiptDetailRepository exportReceiptDetailRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private BillRepository billRepository;
    @Autowired
    private ExportReceiptService exportReceiptService;
    @Autowired
    private ImportReceiptDetailRepository importReceiptDetailRepository;

    public ResponseEntity<Result> createAndUpdate(OrderRequest request) {
        try {
            return ResponseEntity.ok(new Result("SUCCESS", "OK", createAndUpdateOrder(request)));
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Result(e.getMessage(), "NOT_FOUND", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Result(e.getMessage(), "CONFLICT", null));
        }
    }

    private List<OrderDetailEntity> createAndUpdateOrder(OrderRequest request) {
        OrderEntity order = null;
        List<OrderDetailEntity> orderDetailEntityList = new ArrayList<>();
        if (request.getId() == null) {
            order = new OrderEntity();
        } else {
            order = orderRepository.findById(request.getId()).orElse(null);
            if (order == null) {
                throw new NullPointerException("Not found order");
            }
            orderDetailEntityList = orderDetailRepository.findOrderDetailEntitiesByOrderId(request.getId());
            orderDetailRepository.deleteAll(orderDetailEntityList);
            orderDetailEntityList.clear();
            order.setUpdatedDate(new Date());
        }
        order.setOrderDate(new Date());
        order.setOrderStatus(OrderStatus.IN_PROGRESS.getName());
        order.setCustomerId(request.getCustomerId());
        orderRepository.save(order);
        for (OrderProductRequest productRequest : request.getOrderProductRequestList()) {
            OrderDetailEntity orderDetail = new OrderDetailEntity();
            orderDetail.setProductId(productRequest.getProductId());
            orderDetail.setQuantity(productRequest.getQuantity());
            ProductEntity product = productRepository.findById(productRequest.getProductId()).orElse(null);
            if (product == null) {
                throw new NullPointerException("Not found product");
            }
            long quantityInWareHouse = importReceiptDetailRepository.getQuantityProductInWareHouseById(productRequest.getProductId());
            long quantityExport = exportReceiptDetailRepository.getQuantityProductExportById(productRequest.getProductId());
            if ((quantityInWareHouse - quantityExport) < productRequest.getQuantity()) {
                orderRepository.deleteById(order.getId());
                throw new RuntimeException("Not enough product: " + product.getProductName());
            }
            orderDetail.setOrderId(order.getId());
            BigDecimal totalPrice = product.getPrice().multiply(BigDecimal.valueOf(productRequest.getQuantity()));
            orderDetail.setTotalPrice(totalPrice);
            orderDetailEntityList.add(orderDetail);
        }
//        exportReceiptService.saveExportReceipt(request, order.getId());
        return orderDetailRepository.saveAll(orderDetailEntityList);
    }

    public ResponseEntity<Result> cancelOrder(Long id) {
        try {
            OrderEntity order = orderRepository.findById(id).orElse(null);
            if (order == null) {
                throw new NullPointerException("Not found order");
            }
            order.setOrderStatus(OrderStatus.CANCEL.getName());
            order.setUpdatedDate(new Date());
            return ResponseEntity.ok(new Result("SUCCESS", "OK", orderRepository.save(order)));
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Result(e.getMessage(), "NOT_FOUND", null));
        }
    }
}
