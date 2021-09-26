package com.lyn521.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lyn521.pojo.Order;

public interface OrderService {
    //获取全部订单
    Object getAll(int page, int limit);

    //根据id获取订单
    Object getOrderById(int id);

    //根据id删除订单
    Object delOrderById(String id);

    //添加订单
    Object addOrder(Order order);

    //更新订单
    Object restOrder(Order order);

    //更改订单状态
    Object restState(int id, Boolean state);

    //根据商户id查找订单(分页)
    Object getAllBySId(int page, int limit, int id);
    //添加订单
    Object setNewOrder(Order order) throws JsonProcessingException;
    //根据用户id获取订单
    Object getAllOrder(int id);
    //订单状态
    Object removeOrder(int id);

    //订单进度
    Object setStates(int id,int states);
}
