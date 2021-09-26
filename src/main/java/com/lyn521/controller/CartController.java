package com.lyn521.controller;

import com.lyn521.pojo.Cart;
import com.lyn521.pub.back.MsgCode;
import com.lyn521.pub.back.Result;
import com.lyn521.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;
    @Autowired(required = false)
    private HttpServletRequest request;
    @GetMapping("my")
    public Object getCartByUid(){
        int id=request.getIntHeader("id");
        if (id==0){
            return Result.error(MsgCode.NUM_ERROR);
        }
        return cartService.getCartByUid(id);
    }
    @PostMapping("add")
    public Object addCart(Cart cart){
        cart.setUid(request.getIntHeader("id"));
        return cartService.addGoodsForCart(cart);
    }
    @PostMapping("remove")
    public Object removeCart(Cart cart){
        cart.setUid(request.getIntHeader("id"));
        return cartService.removeCart(cart);
    }
}
