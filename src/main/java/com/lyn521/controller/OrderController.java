package com.lyn521.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lyn521.pojo.Order;
import com.lyn521.pub.tool.SHA;
import com.lyn521.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired(required = false)
    private HttpServletRequest request;

    @RequestMapping("/getAll")
    public Object getAll(int page, int limit) {
        return orderService.getAll(page, limit);
    }

    @GetMapping ("getAllBySId")
    public Object getAllBySId(int page,int limit){
        int id = request.getIntHeader("id");
        return orderService.getAllBySId(page,limit,id);
    }

    @PostMapping("/fast/enable")
    public Object UploadEnable(int id ,boolean enable){
        return orderService.restState(id,enable);
    }

    @RequestMapping("/getById")
    public Object getOrderById(int id) {
        return orderService.getOrderById(id);
    }

    @RequestMapping("/add")
    public Object addOrder(Order order) {
        return orderService.addOrder(order);
    }

    @RequestMapping("/del")
    public Object delOrderById(String id){
        return orderService.delOrderById(id);
    }

    @RequestMapping("/rest")
    public Object restOrder(Order order){
        return orderService.restOrder(order);
    }

    @PostMapping("push")
    public Object pushNewOrder(Order order) throws JsonProcessingException {
        order.setUserId(request.getIntHeader("id"));
        order.setTime(SHA.getNowTime());
        System.out.println(order);
        return orderService.setNewOrder(order);
    }
    @PostMapping("getAllById")
    public Object getAllOrder(){
        int id=request.getIntHeader("id");
        return orderService.getAllOrder(id);
    }
    @PostMapping("/remove")
    public Object remove(Order order){
        System.out.println("======================="+order.getId());
        return orderService.removeOrder(order.getId());
    }

    @PostMapping("/setStates")
    public Object setStates(int id,int states){
        return orderService.setStates(id,states);
    }
}
