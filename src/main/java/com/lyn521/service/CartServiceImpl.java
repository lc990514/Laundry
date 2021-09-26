package com.lyn521.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.lyn521.mapper.CartMapper;
import com.lyn521.mapper.SetMealMapper;
import com.lyn521.pojo.Cart;
import com.lyn521.pojo.SetMeal;
import com.lyn521.pub.back.MsgCode;
import com.lyn521.pub.back.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private SetMealMapper setMealMapper;

    @Override
    public Object getCartByUid(int id) {
        LambdaQueryWrapper<Cart> cartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        cartLambdaQueryWrapper.eq(Cart::getUid, id);
        List<Cart> carts = cartMapper.selectList(cartLambdaQueryWrapper);
        return Result.success(carts);
    }

    @Override
    public Object addGoodsForCart(Cart cart) {
        System.out.println(cart);
        LambdaQueryWrapper<Cart> cartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        cartLambdaQueryWrapper
                .eq(Cart::getUid, cart.getUid())
                .eq(Cart::getGid, cart.getGid());
        LambdaQueryWrapper<SetMeal> setMealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setMealLambdaQueryWrapper
                .eq(SetMeal::getId, cart.getGid())
                .select(SetMeal::getId);
        SetMeal setMeal = setMealMapper.selectOne(setMealLambdaQueryWrapper);
        if (setMeal == null) {
            //商品没有
            return Result.error(MsgCode.SetMeal_NULL);
        }
        Cart cart1 = cartMapper.selectOne(cartLambdaQueryWrapper);
        //若果购物车有就更新一下
        if (cart1 != null) {
            cart1.setNum(cart1.getNum() + cart.getNum());
            int row = cartMapper.updateById(cart1);
            if (row > 0) {
                return Result.success(cart1);
            } else {
                return Result.error(MsgCode.SERVER_ERROR);
            }
        } else {//购物车没有直接添加
            if (cart.getNum() <= 0) {
                //商品数量绑定异常
                return Result.error(MsgCode.NUM_ERROR);
            }
            int row = cartMapper.insert(cart);
            if (row > 0) {
                return Result.success(cart);
            } else {
                return Result.error(MsgCode.SERVER_ERROR);
            }
        }
    }

    @Override
    public Object removeCart(Cart cart) {
        LambdaUpdateWrapper<Cart> cartLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        cartLambdaUpdateWrapper
                .eq(Cart::getId, cart.getId())
                .eq(Cart::getUid, cart.getUid());
        int row = cartMapper.delete(cartLambdaUpdateWrapper);
        if (row > 0) {
            return Result.success(cart.getId());
        }else {
            return Result.error(MsgCode.SERVER_ERROR);
        }
    }

    @Override
    public int removeCartById(List<Integer> list) {
        LambdaUpdateWrapper<Cart> cartLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        cartLambdaUpdateWrapper
                .in(Cart::getId,list);
        return cartMapper.delete(cartLambdaUpdateWrapper);
    }
}
