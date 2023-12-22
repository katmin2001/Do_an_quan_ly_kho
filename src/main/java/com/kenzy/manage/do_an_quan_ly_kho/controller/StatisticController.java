package com.kenzy.manage.do_an_quan_ly_kho.controller;

import com.kenzy.manage.do_an_quan_ly_kho.entity.constant.Result;
import com.kenzy.manage.do_an_quan_ly_kho.service.StatisticService;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/statistic")
public class StatisticController {
    @Resource
    private StatisticService statisticService;

    @GetMapping("/get")
    public ResponseEntity<Result> getStatistic() {
        return statisticService.statistic();
    }

    @GetMapping("/get-by/{choose}")
    public ResponseEntity<Result> getStatistic(@PathVariable("choose") String choose) {
        return statisticService.statisticBy(choose);
    }
}
