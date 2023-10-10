package com.kenzy.manage.do_an_quan_ly_kho.controller;

import com.kenzy.manage.do_an_quan_ly_kho.entity.constant.Result;
import com.kenzy.manage.do_an_quan_ly_kho.model.request.SearchRequest;
import com.kenzy.manage.do_an_quan_ly_kho.model.request.WareHouseRequest;
import com.kenzy.manage.do_an_quan_ly_kho.service.WareHouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/warehouse")
public class WareHouseController {
    @Autowired
    private WareHouseService wareHouseService;

    @PostMapping("/create")
    public ResponseEntity<Result> create(@RequestBody WareHouseRequest request) {
        return wareHouseService.createAndEditWareHouse(request);
    }

    @PostMapping("/edit")
    public ResponseEntity<Result> edit(@RequestBody WareHouseRequest request) {
        return wareHouseService.createAndEditWareHouse(request);
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<Result> delete(@PathVariable Long id) {
        return wareHouseService.delete(id);
    }

    @GetMapping("/active/{id}")
    public ResponseEntity<Result> active(@PathVariable Long id) {
        return wareHouseService.active(id);
    }

    @PostMapping("/search")
    public ResponseEntity<Result> search(@RequestBody SearchRequest request){
        return wareHouseService.search(request);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Result> detail(@PathVariable("id") Long id){
        return wareHouseService.getDetailWareHouse(id);
    }
}
