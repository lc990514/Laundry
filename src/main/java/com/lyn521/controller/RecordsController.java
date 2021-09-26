package com.lyn521.controller;

import com.lyn521.service.RecordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/records")
public class RecordsController {
    @Autowired
    private RecordsService recordsService;

    @PostMapping("/getById")
    public Object getById(int id){
        return recordsService.getRecordsById(id);
    }
}
