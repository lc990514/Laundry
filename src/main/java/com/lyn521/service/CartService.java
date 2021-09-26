package com.lyn521.service;

import com.lyn521.pojo.Cart;

import java.util.List;

public interface CartService {
    Object getCartByUid(int id);
    Object addGoodsForCart(Cart cart);
    Object removeCart(Cart cart);
    int removeCartById(List<Integer> list);
}
