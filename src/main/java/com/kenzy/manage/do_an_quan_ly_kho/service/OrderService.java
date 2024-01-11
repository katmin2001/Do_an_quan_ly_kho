package com.kenzy.manage.do_an_quan_ly_kho.service;

import com.kenzy.manage.do_an_quan_ly_kho.entity.*;
import com.kenzy.manage.do_an_quan_ly_kho.entity.constant.OrderStatus;
import com.kenzy.manage.do_an_quan_ly_kho.entity.constant.Result;
import com.kenzy.manage.do_an_quan_ly_kho.model.request.OrderProductRequest;
import com.kenzy.manage.do_an_quan_ly_kho.model.request.OrderRequest;
import com.kenzy.manage.do_an_quan_ly_kho.model.request.SearchRequest;
import com.kenzy.manage.do_an_quan_ly_kho.model.response.MetaList;
import com.kenzy.manage.do_an_quan_ly_kho.model.response.OrderDetailResponse;
import com.kenzy.manage.do_an_quan_ly_kho.model.response.OrderResponse;
import com.kenzy.manage.do_an_quan_ly_kho.model.response.SearchResponse;
import com.kenzy.manage.do_an_quan_ly_kho.repository.*;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    private BillRepository billRepository;
    @Autowired
    private ExportReceiptService exportReceiptService;
    @Autowired
    private ImportReceiptDetailRepository importReceiptDetailRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ProductService productService;
    @Resource
    private EmailService mailService;

    public ResponseEntity<Result> createAndUpdate(OrderRequest request) {
        try {
            return ResponseEntity.ok(new Result("SUCCESS", "OK", createAndUpdateOrder(request)));
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Result(e.getMessage(), "NOT_FOUND", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new Result(e.getMessage(), "CONFLICT", null));
        }
    }

    public ResponseEntity<Result> sendMailOrder(OrderResponse order) {
        try {
            mailService.sendMail(order);
            return ResponseEntity.ok(new Result("SUCCESS", "OK", null));
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Result(e.getMessage(), "NOT_FOUND", null));
        }
    }

    private List<OrderDetailEntity> createAndUpdateOrder(OrderRequest request) {
        OrderEntity order = null;
        BillEntity bill = null;
        boolean isCreate = true;
        List<OrderDetailEntity> orderDetailEntityList = new ArrayList<>();
        if (request.getId() == null) {
            order = new OrderEntity();
            order.setOrderStatus(OrderStatus.IN_PROGRESS.getType());
            bill = new BillEntity();
            bill.setCreatedBy(getNameByToken());
            bill.setCreatedDate(new Date());
        } else {
            isCreate = false;
            order = orderRepository.findById(request.getId()).orElse(null);
            if (order == null) {
                throw new NullPointerException("Not found order");
            }
            if (order.getOrderStatus() == OrderStatus.CANCEL.getType() || order.getOrderStatus() == OrderStatus.RETURNED_REFUNDED.getType()) {
                throw new RuntimeException("Canceled orders cannot be edited");
            }
            if (order.getOrderStatus() == OrderStatus.SHIPPING.getType() && request.getOrderStatus() == OrderStatus.IN_PROGRESS.getType()) {
                throw new RuntimeException("Shipping orders cannot be edited to process");
            }
            if (request.getOrderStatus() == OrderStatus.RETURNED_REFUNDED.getType()) {
                ExportReceiptEntity exportReceipt = exportReceiptRepository.findExportReceiptEntityByOrderId(request.getId());
                if (exportReceipt == null) {
                    throw new NullPointerException("Not found export receipt");
                }
                exportReceipt.setStatus(false);
                List<ExportReceiptDetailEntity> exportReceiptDetailEntityList = exportReceiptDetailRepository.findExportReceiptDetailEntitiesByExportReceiptId(exportReceipt.getId());
                for (ExportReceiptDetailEntity exportReceiptDetail : exportReceiptDetailEntityList) {
                    exportReceiptDetail.setStatus(false);
                }
                exportReceiptDetailRepository.saveAll(exportReceiptDetailEntityList);
                exportReceiptRepository.save(exportReceipt);
            }
            orderDetailEntityList = orderDetailRepository.findOrderDetailEntitiesByOrderId(request.getId());
            orderDetailRepository.deleteAll(orderDetailEntityList);
            orderDetailEntityList.clear();
            order.setUpdatedDate(new Date());
            order.setUpdatedBy(getNameByToken());
            order.setOrderStatus(request.getOrderStatus());
            bill = billRepository.findBillEntityByOrderId(order.getId());
            bill.setUpdatedBy(getNameByToken());
            bill.setUpdatedDate(new Date());
        }
        order.setOrderDate(new Date());
        order.setCustomerId(request.getCustomerId());
        order.setCreatedBy(getNameByToken());
        orderRepository.save(order);
        order.setCode(createCode("ORD", order.getId()));
        orderRepository.save(order);
        BigDecimal totalAmount = BigDecimal.valueOf(0);
        for (OrderProductRequest productRequest : request.getOrderProductRequestList()) {
            OrderDetailEntity orderDetail = new OrderDetailEntity();
            orderDetail.setProductId(productRequest.getProductId());
            orderDetail.setQuantity(productRequest.getQuantity());
            ProductEntity product = productRepository.findById(productRequest.getProductId()).orElse(null);
            if (product == null) {
                throw new NullPointerException("Not found product");
            }
            Integer quantityInWareHouse = importReceiptDetailRepository.getQuantityProductInWareHouseById(productRequest.getProductId());
            if (quantityInWareHouse == null) {
                quantityInWareHouse = 0;
            }
            Integer quantityExport = exportReceiptDetailRepository.getQuantityProductExportById(productRequest.getProductId());
            if (quantityExport == null) {
                quantityExport = 0;
            }
            if ((quantityInWareHouse - quantityExport) < productRequest.getQuantity()) {
                if (isCreate) {
                    orderRepository.deleteById(order.getId());
                    throw new RuntimeException("Not enough product: " + product.getProductName());
                } else {
                    throw new RuntimeException("Not enough product: " + product.getProductName());
                }
            }
            orderDetail.setOrderId(order.getId());
            BigDecimal totalPrice = product.getPrice().multiply(BigDecimal.valueOf(productRequest.getQuantity()));
            orderDetail.setTotalPrice(totalPrice);
            totalAmount = totalAmount.add(totalPrice);
            orderDetail.setCreatedBy(getNameByToken());
            orderDetail.setUpdatedBy(getNameByToken());
            orderDetailEntityList.add(orderDetail);
        }
        order.setTotalAmount(totalAmount);
        orderRepository.save(order);
        //create and edit bill
        bill.setTotalPrice(totalAmount);
        bill.setOrderId(order.getId());
        bill.setPaymentAmount(request.getPaymentAmount());
        bill.setPaymentDate(request.getPaymentDate());
        bill.setPaymentMethod(request.getPaymentMethod());
        billRepository.save(bill);
        CustomerEntity customer = customerRepository.findById(order.getCustomerId()).orElse(null);
        if (customer == null) {
            throw new NullPointerException("Not found customer");
        }
//        mailService.sendMail(order, customer, orderDetailEntityList);
//        exportReceiptService.saveExportReceipt(request, order.getId());
        return orderDetailRepository.saveAll(orderDetailEntityList);
    }

//    private void sendMail(OrderResponse order) {
//        mailService.sendMail(order);
//    }

    public ResponseEntity<Result> cancelOrder(Long id) {
        try {
            OrderEntity order = orderRepository.findById(id).orElse(null);
            if (order == null) {
                throw new NullPointerException("Not found order");
            }
            order.setOrderStatus(OrderStatus.CANCEL.getType());
            order.setStatus(false);
            List<OrderDetailEntity> orderDetailEntityList = orderDetailRepository.findOrderDetailEntitiesByOrderId(order.getId());
            for (OrderDetailEntity orderDetail : orderDetailEntityList) {
                orderDetail.setStatus(false);
            }
            order.setUpdatedDate(new Date());
            ExportReceiptEntity exportReceipt = exportReceiptRepository.findExportReceiptEntityByOrderId(id);
            if (exportReceipt != null) {
                exportReceipt.setStatus(false);
                List<ExportReceiptDetailEntity> exportReceiptDetailEntityList = exportReceiptDetailRepository.findExportReceiptDetailEntitiesByExportReceiptId(exportReceipt.getId());
                for (ExportReceiptDetailEntity exportReceiptDetail : exportReceiptDetailEntityList) {
                    exportReceiptDetail.setStatus(false);
                }
                exportReceiptDetailRepository.saveAll(exportReceiptDetailEntityList);
                exportReceiptRepository.save(exportReceipt);
            }
            orderDetailRepository.saveAll(orderDetailEntityList);
            return ResponseEntity.ok(new Result("SUCCESS", "OK", orderRepository.save(order)));
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Result(e.getMessage(), "NOT_FOUND", null));
        }
    }

    public ResponseEntity<Result> detailOrder(Long id) {
        try {
            return ResponseEntity.ok(new Result("SUCCESS", "OK", detail(id)));
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Result(e.getMessage(), "NOT_FOUND", null));
        }
    }

    public ResponseEntity<Result> searchOrder(SearchRequest request) {
        MetaList metaList = request.getMeta();
        Pageable pageable = buildPageable(request.getMeta(), "created_date", true);
        Page<OrderEntity> page = orderRepository.search(request.getKeyword(), request.getFromDate(), request.getToDate(), pageable);
        List<OrderResponse> responses = new ArrayList<>();
        for (OrderEntity order : page) {
            responses.add(mapperOrder(order));
        }
        metaList.setTotal(page.getTotalElements());
        SearchResponse<OrderResponse> response = new SearchResponse<>(responses, metaList);
        return ResponseEntity.ok(new Result("SUCCESS", "OK", response));
    }

    public ResponseEntity<Result> getDetailOrderByOrderId(Long id) {
        try {
            OrderEntity order = orderRepository.findById(id).orElse(null);
            if (order == null) {
                throw new NullPointerException("Not found order");
            }
            List<OrderDetailEntity> orderDetailEntityList = orderDetailRepository.findOrderDetailEntitiesByOrderId(id);
            List<OrderDetailResponse> responses = new ArrayList<>();
            for (OrderDetailEntity orderDetail : orderDetailEntityList) {
                responses.add(mapperOrderDetail(orderDetail));
            }
            return ResponseEntity.ok(new Result("SUCCESS", "OK", responses));
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Result(e.getMessage(), "NOT_FOUND", null));
        }
    }

    public OrderResponse detail(Long id) {
        OrderEntity order = orderRepository.findById(id).orElse(null);
        if (order == null) {
            throw new NullPointerException("Not found order");
        }
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setOrderDate(order.getOrderDate());
        response.setOrderStatus(order.getOrderStatus());
        CustomerEntity customer = customerRepository.findById(order.getCustomerId()).orElse(null);
        if (customer == null) {
            throw new NullPointerException("Not found customer");
        }
        response.setCustomer(customer);
        List<OrderDetailEntity> orderDetailEntityList = orderDetailRepository.findOrderDetailEntitiesByOrderId(id);
        List<OrderDetailResponse> orderDetailResponseList = new ArrayList<>();
        for (OrderDetailEntity orderDetail : orderDetailEntityList) {
            orderDetailResponseList.add(mapperOrderDetail(orderDetail));
        }
        response.setOrderDetailResponseList(orderDetailResponseList);
        response.setTotalAmount(order.getTotalAmount());
        return response;
    }

    private OrderResponse mapperOrder(OrderEntity order) {
        CustomerEntity customer = customerRepository.findById(order.getCustomerId()).orElse(null);
        if (customer == null) {
            throw new NullPointerException("Not found customer");
        }
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setCode(order.getCode());
        response.setOrderDate(order.getOrderDate());
        response.setOrderStatus(order.getOrderStatus());
        response.setCustomer(customer);
        response.setTotalAmount(order.getTotalAmount());
        response.setCreatedBy(order.getCreatedBy());
        List<OrderDetailResponse> responses = new ArrayList<>();
        List<OrderDetailEntity> orderDetailEntityList = orderDetailRepository.findOrderDetailEntitiesByOrderId(order.getId());
        for (OrderDetailEntity orderDetail : orderDetailEntityList) {
            responses.add(mapperOrderDetail(orderDetail));
        }
        BillEntity bill = billRepository.findBillEntityByOrderId(order.getId());
        response.setBill(bill);
        response.setOrderDetailResponseList(responses);
        return response;
    }

    private OrderDetailResponse mapperOrderDetail(OrderDetailEntity orderDetail) {
        OrderDetailResponse response = new OrderDetailResponse();
        ProductEntity product = productRepository.findById(orderDetail.getProductId()).orElse(null);
        if (product == null) {
            throw new NullPointerException("Not found product");
        }
        response.setProductId(orderDetail.getProductId());
        response.setPriceUnit(product.getPrice());
        response.setImageUrls(List.of(product.getProductImages()));
        response.setProductName(product.getProductName());
        response.setQuantity(orderDetail.getQuantity());
        response.setTotalPrice(orderDetail.getTotalPrice());
        return response;
    }
}
