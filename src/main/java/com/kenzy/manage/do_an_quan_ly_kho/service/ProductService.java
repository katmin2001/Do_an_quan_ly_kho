package com.kenzy.manage.do_an_quan_ly_kho.service;

import com.kenzy.manage.do_an_quan_ly_kho.entity.CategoryEntity;
import com.kenzy.manage.do_an_quan_ly_kho.entity.ProductEntity;
import com.kenzy.manage.do_an_quan_ly_kho.entity.SupplierEntity;
import com.kenzy.manage.do_an_quan_ly_kho.entity.constant.Result;
import com.kenzy.manage.do_an_quan_ly_kho.model.request.ProductRequest;
import com.kenzy.manage.do_an_quan_ly_kho.model.request.ProductSearchRequest;
import com.kenzy.manage.do_an_quan_ly_kho.model.response.MetaList;
import com.kenzy.manage.do_an_quan_ly_kho.model.response.ProductResponse;
import com.kenzy.manage.do_an_quan_ly_kho.model.response.SearchResponse;
import com.kenzy.manage.do_an_quan_ly_kho.repository.*;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
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
import java.util.Arrays;
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
    @Resource
    private ExcelService excelService;

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

    public ResponseEntity<Result> importProductsFromExcel(MultipartFile file) throws IOException {
        List<List<String>> rows = excelService.readExcel(file);
        List<ProductEntity> products = new ArrayList<>();
        int indexProductName = 0, indexPrice = 0, indexSupplier = 0, indexCategory = 0, indexDescription = 0;
        for (int i = 0; i < rows.get(0).size(); i++) {
            switch (rows.get(0).get(i)) {
                case "Tên sản phẩm":
                    indexProductName = i;
                    break;
                case "Giá":
                    indexPrice = i;
                    break;
                case "Nhà cung cấp":
                    indexSupplier = i;
                    break;
                case "Loại hàng":
                    indexCategory = i;
                    break;
                case "Mô tả":
                    indexDescription = i;
                    break;
            }
        }
        rows.remove(0);
        for (List<String> row : rows) {
            if (row.get(0) == "") {
                break;
            }
            String productName = row.get(indexProductName);
            BigDecimal price = BigDecimal.valueOf(Double.parseDouble(row.get(indexPrice)));
            String supplierName = row.get(indexSupplier);
            String categoryName = row.get(indexCategory);
            String description = row.get(indexDescription);

            ProductEntity product = new ProductEntity();
            product.setProductName(productName);
            product.setPrice(price);
            product.setDescription(description);
            CategoryEntity category = categoryRepository.findCategoryEntityByName(categoryName);
            if (category == null) {
                category = new CategoryEntity();
                category.setName(categoryName);
                category.setCreatedDate(new Date());
                category.setCreatedBy(getNameByToken());
                categoryRepository.save(category);
            }
            product.setCategoryId(category.getId());
            SupplierEntity supplier = supplierRepository.findSupplierEntityByContactName(supplierName);
            if (supplier == null) {
                supplier = new SupplierEntity();
                supplier.setContactName(supplierName);
                supplier.setCreatedBy(getNameByToken());
                supplier.setCreatedDate(new Date());
                supplierRepository.save(supplier);
            }
            product.setSupplierId(supplier.getId());
            product.setCreatedDate(new Date());
            product.setCreatedBy(getNameByToken());

            products.add(product);
            productRepository.save(product);
        }
        return ResponseEntity.ok(new Result("SUCCESS", "OK", null));
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
            if (files != null) {
                List<String> productImages = new ArrayList<>();
                if (files.size() != 0 && !files.get(0).getOriginalFilename().equals("")) {
                    for (MultipartFile file : files) {
                        String url = FileService.uploadFile(file, UPLOAD_DIR);
                        productImages.add(url);
                    }
                }
                String[] listUrl = productImages.toArray(new String[0]);
                product.setProductImages(listUrl);
            }
        } else {
            product = productRepository.findById(request.getId()).orElseThrow(null);
            product.setUpdatedDate(new Date());
            product.setUpdatedBy(getNameByToken());
//            String[] productImages = product.getProductImages();
            String[] urls = product.getProductImages();
            List<String> imageUrls = Arrays.asList(request.getProductImages());
            List<String> newUrls = new ArrayList<>();
            if (urls != null && imageUrls != null) {
                for (String url : urls) {
                    if (imageUrls.contains(url)) {
                        newUrls.add(url);
                    } else {
                        FileService.deleteFile(url);
                    }
                }
            }

            if (files != null) {
                if (files.size() != 0 && !files.get(0).getOriginalFilename().equals("")) {
                    for (MultipartFile file : files) {
                        String url = FileService.uploadFile(file, UPLOAD_DIR);
                        newUrls.add(url);
                    }
                }
            }
            product.setProductImages(newUrls.toArray(new String[0]));
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
        product.setCreatedBy(getNameByToken());
        product.setUpdatedBy(getNameByToken());
        return productRepository.save(product);
    }
}
