package com.lyn521.controller;

import com.lyn521.pojo.Coupon;
import com.lyn521.pub.back.MsgCode;
import com.lyn521.pub.back.Result;
import com.lyn521.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("coupon")
public class CouponController {
    @Autowired
    private CouponService couponService;
    @Autowired(required = false)
    private HttpServletRequest request;

    @GetMapping("getCouponByUserId")
    public Object getCouponByUserId(){
        int userId = request.getIntHeader("id");
        if (userId==0){
            return Result.error(MsgCode.NUM_ERROR);
        }
        return couponService.getCouponByUserId(userId);
    }
    @PostMapping("exchange")
    public Object exchange(String str){
        int id = request.getIntHeader("id");
        if (id==0){
            return Result.error(MsgCode.NUM_ERROR);
        }
        return couponService.exchange(str,id);
    }

    @PostMapping("del")
    public Object del(Coupon coupon){
        return couponService.del(coupon.getId());
    }

    @PostMapping("getById")
    public Object getById(Coupon coupon){
        return couponService.getById(coupon.getId());
    }
}
