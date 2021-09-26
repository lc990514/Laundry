package com.lyn521.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.lyn521.mapper.ConvertMapper;
import com.lyn521.mapper.CouponMapper;
import com.lyn521.pojo.Convert;
import com.lyn521.pojo.Coupon;
import com.lyn521.pub.back.MsgCode;
import com.lyn521.pub.back.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CouponServiceImpl implements CouponService{
    @Autowired
    private CouponMapper couponMapper;
    @Autowired
    private ConvertMapper convertMapper;

    @Override
    public Object getCouponByUserId(int id) {
        LambdaQueryWrapper<Coupon> couponLambdaQueryWrapper = new LambdaQueryWrapper<>();
        couponLambdaQueryWrapper.select(
                Coupon::getId,
                Coupon::getUserId,
                Coupon::getStartAt,
                Coupon::getEndAt,
                Coupon::getMoney
        ).eq(Coupon::getUserId,id);
        List<Coupon> coupons = couponMapper.selectList(couponLambdaQueryWrapper);
        Long time = getUnixTime(new Date());
        for (int i = 0; i < coupons.size(); i++) {
            if (time<coupons.get(i).getEndAt()){
                coupons.get(i).setState(true);
            }else {
                coupons.get(i).setState(false);
            }
        }
        return Result.success(coupons);
    }

    @Override
    public Object exchange(String str,int id) {
        LambdaQueryWrapper<Convert> convertLambdaQueryWrapper = new LambdaQueryWrapper<>();
        convertLambdaQueryWrapper.select(
                Convert::getMoney
        ).eq(Convert::getStr,str);
        Convert convert = convertMapper.selectOne(convertLambdaQueryWrapper);
        if (convert==null){
            return Result.success(0);
        }
        Coupon coupon = new Coupon();
        coupon.setUserId(id);
        Long time = getUnixTime(new Date());
        coupon.setStartAt(time);
        coupon.setEndAt(time+2592000);
        coupon.setMoney(convert.getMoney());
        int row = couponMapper.insert(coupon);
        if (row<=0){
            return Result.error(MsgCode.SERVER_ERROR);
        }
        return Result.success(1);
    }

    @Override
    public Object del(int id) {
        LambdaUpdateWrapper<Coupon> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(Coupon::getId,id);
        int row = couponMapper.delete(lambdaUpdateWrapper);
        if (row<=0){
            return Result.error(MsgCode.SERVER_ERROR);
        }
        return Result.success(1);
    }

    @Override
    public Object getById(int id) {
        LambdaQueryWrapper<Coupon> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Coupon::getId,id);
        Coupon coupon = couponMapper.selectOne(lambdaQueryWrapper);
        if (coupon==null){
            return Result.error(MsgCode.SERVER_ERROR);
        }
        return Result.success(coupon);
    }


    //获取时间戳
    public static long getUnixTime(Date date)
    {
        if( null == date )

        {
            return 0;
        }
        return date.getTime()/1000;
    }
}
