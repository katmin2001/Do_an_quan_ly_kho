package com.kenzy.manage.do_an_quan_ly_kho.service;

import com.kenzy.manage.do_an_quan_ly_kho.entity.ProductEntity;
import com.kenzy.manage.do_an_quan_ly_kho.entity.constant.Result;
import com.kenzy.manage.do_an_quan_ly_kho.model.request.ProductRequest;
import com.kenzy.manage.do_an_quan_ly_kho.model.request.ProductSearchRequest;
import com.kenzy.manage.do_an_quan_ly_kho.model.request.SearchRequest;
import com.kenzy.manage.do_an_quan_ly_kho.model.response.MetaList;
import com.kenzy.manage.do_an_quan_ly_kho.model.response.ProductResponse;
import com.kenzy.manage.do_an_quan_ly_kho.model.response.SearchResponse;
import com.kenzy.manage.do_an_quan_ly_kho.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ProductService extends BaseService {
    private final String UPLOAD_DIR = "upload/product-images";
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private ImportReceiptDetailRepository importReceiptDetailRepository;
    @Autowired
    private ExportReceiptDetailRepository exportReceiptDetailRepository;

    public ResponseEntity<Result> createAndEditProduct(ProductRequest request, List<MultipartFile> files) {
        try {
            return ResponseEntity.ok(new Result("SUCCESS", "OK", saveProduct(request, files)));
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Result("NOT FOUND PRODUCT", "NOT_FOUND", null));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Result(e.getMessage(), "NOT_FOUND", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Result(e.getMessage(), "NOT_FOUND", null));
        }
    }

    public ResponseEntity<Result> detail(Long id) {
        try {
            return ResponseEntity.ok(new Result("SUCCESS", "OK", detailProduct(id)));
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Result("NOT FOUND PRODUCT", "NOT_FOUND", null));
        }
    }

    public ResponseEntity<Result> inactiveProduct(Long id) {
        try {
            return ResponseEntity.ok(new Result("SUCCESS", "OK", inactive(id)));
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Result("NOT FOUND PRODUCT", "NOT_FOUND", null));
        }
    }

    public ResponseEntity<Result> activeProduct(Long id) {
        try {
            return ResponseEntity.ok(new Result("SUCCESS", "OK", active(id)));
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Result("NOT FOUND PRODUCT", "NOT_FOUND", null));
        }
    }

    public ResponseEntity<Result> search(ProductSearchRequest request) {
        MetaList metaList = request.getMeta();
        Pageable pageable = buildPageable(request.getMeta(), "created_date", true);
        Page<ProductEntity> page = productRepository.search(request.getKeyword(), request.getFromDate(), request.getToDate(), request.getCategoryId(), request.getSupplierId(), pageable);
        List<ProductResponse> responses = new ArrayList<>();
        for (ProductEntity product : page) {
            ProductResponse response = new ProductResponse();
            Integer quantityInWareHouse = importReceiptDetailRepository.getQuantityProductInWareHouseById(product.getId());
            if (quantityInWareHouse == null) {
                quantityInWareHouse = 0;
            }
            Integer quantityExport = exportReceiptDetailRepository.getQuantityProductExportById(product.getId());
            if (quantityExport == null) {
                quantityExport = 0;
            }
            response.setQuantity(quantityInWareHouse - quantityExport);
            response.setProductName(product.getProductName());
            response.setId(product.getId());
            response.setPrice(product.getPrice());
            response.setDescription(product.getDescription());
            response.setProductImages(product.getProductImages());
            response.setSupplierName(supplierRepository.findById(product.getSupplierId()).get().getContactName());
            response.setCategoryName(categoryRepository.findById(product.getCategoryId()).get().getName());
            responses.add(response);
        }
        metaList.setTotal(page.getTotalElements());
        SearchResponse<ProductResponse> response = new SearchResponse<>(responses, metaList);
        return ResponseEntity.ok(new Result("SUCCESS", "OK", response));
    }

    private ProductEntity inactive(Long id) {
        ProductEntity product = productRepository.findById(id).orElseThrow(null);
        product.setStatus(false);
        product.setUpdatedDate(new Date());
        return productRepository.save(product);
    }

    private ProductEntity active(Long id) {
        ProductEntity product = productRepository.findById(id).orElseThrow(null);
        product.setStatus(true);
        product.setUpdatedDate(new Date());
        return productRepository.save(product);
    }

    public ProductResponse detailProduct(Long id) {
        ProductEntity product = productRepository.findById(id).orElse(null);
        if (product == null) {
            throw new NullPointerException("Not found product");
        }
        ProductResponse response = new ProductResponse();
        response.setId(id);
        response.setProductName(product.getProductName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setProductImages(product.getProductImages());
        response.setSupplierName(supplierRepository.findById(product.getSupplierId()).get().getContactName());
        response.setCategoryName(categoryRepository.findById(product.getCategoryId()).get().getName());
        return response;
    }

    private ProductEntity saveProduct(ProductRequest request, List<MultipartFile> files) throws IOException {
        ProductEntity product = null;
        if (request.getId() == null) {
            product = new ProductEntity();
        } else {
            product = productRepository.findById(request.getId()).orElseThrow(null);
            product.setUpdatedDate(new Date());
            product.setUpdatedBy(getNameByToken());
            String[] urls = product.getProductImages();
            for (String url : urls) {
                FileService.deleteFile(url);
            }
        }
        product.setProductName(request.getProductName());
        product.setPrice(request.getPrice());
        product.setDescription(request.getDescription());
        if (categoryRepository.existsCategoryEntityById(request.getCategoryId())) {
            product.setCategoryId(request.getCategoryId());
        } else {
            throw new RuntimeException("Not found category");
        }
        if (supplierRepository.existsSupplierEntityById(request.getSupplierId())) {
            product.setSupplierId(request.getSupplierId());
        } else {
            throw new RuntimeException("Not found supplier");
        }
        List<String> productImages = new ArrayList<>();
        if (files.size() != 0 && !files.get(0).getOriginalFilename().equals("")) {
            for (MultipartFile file : files) {
                String url = FileService.uploadFile(file, UPLOAD_DIR);
                productImages.add(url);
            }
        }
        String[] listUrl = productImages.toArray(new String[0]);
        product.setProductImages(listUrl);
        product.setCreatedBy(getNameByToken());
        product.setUpdatedBy(getNameByToken());
        return productRepository.save(product);
    }
}
