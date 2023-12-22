package com.kenzy.manage.do_an_quan_ly_kho.service;

import com.kenzy.manage.do_an_quan_ly_kho.entity.ExportReceiptDetailEntity;
import com.kenzy.manage.do_an_quan_ly_kho.entity.ImportReceiptDetailEntity;
import com.kenzy.manage.do_an_quan_ly_kho.entity.constant.Result;
import com.kenzy.manage.do_an_quan_ly_kho.model.response.IGetStatisticByCategoryOrProduct;
import com.kenzy.manage.do_an_quan_ly_kho.model.response.StatisticResponse;
import com.kenzy.manage.do_an_quan_ly_kho.repository.*;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class StatisticService extends BaseService {
    @Resource
    private ImportReceiptRepository importReceiptRepository;
    @Resource
    private ExportReceiptRepository exportReceiptRepository;
    @Resource
    private ImportReceiptDetailRepository importReceiptDetailRepository;
    @Resource
    private ExportReceiptDetailRepository exportReceiptDetailRepository;
    @Resource
    private CategoryRepository categoryRepository;
    @Resource
    private ProductRepository productRepository;

    public ResponseEntity<Result> statistic() {
        return ResponseEntity.ok().body(new Result("SUCCESS", "OK", getStatistic()));
    }

    public ResponseEntity<Result> statisticBy(String choose) {
        return ResponseEntity.ok().body(new Result("SUCCESS", "OK", getStatisticBy(choose)));
    }

    private StatisticResponse getStatistic() {
        List<ImportReceiptDetailEntity> importReceiptDetailEntityList = importReceiptDetailRepository.findImportReceiptDetailEntitiesByStatus(true);
        List<ExportReceiptDetailEntity> exportReceiptDetailRepositoryList = exportReceiptDetailRepository.findExportReceiptDetailEntitiesByStatus(true);
        BigDecimal revenue = BigDecimal.valueOf(0);
        BigDecimal costs = BigDecimal.valueOf(0);
        for (ImportReceiptDetailEntity importReceiptDetail : importReceiptDetailEntityList) {
            costs = costs.add(importReceiptDetail.getTotalPrice());
        }
        for (ExportReceiptDetailEntity exportReceiptDetail : exportReceiptDetailRepositoryList) {
            revenue = revenue.add(exportReceiptDetail.getTotalPrice());
        }
        BigDecimal profit = revenue.subtract(costs);
        StatisticResponse response = new StatisticResponse(revenue, costs, profit);
        return response;
    }

    private List<IGetStatisticByCategoryOrProduct> getStatisticBy(String choose) {
        List<IGetStatisticByCategoryOrProduct> getStatisticByCategoryOrProducts = new ArrayList<>();
        if (choose.equals("category")) {
            getStatisticByCategoryOrProducts = categoryRepository.getStatisticByCategory();
        } else {
            getStatisticByCategoryOrProducts = productRepository.getStatisticByProduct();
        }
        return getStatisticByCategoryOrProducts;
    }
}
