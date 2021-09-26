package com.lyn521.service;

import com.lyn521.pojo.Coupon;

public interface CouponService {
    /**
     * 根据用户id获取优惠券
     * @param id
     * @return
     */
    Object getCouponByUserId(int id);

    /**
     * 兑换优惠券
     * @param str
     * @return
     */
    Object exchange(String str,int id);

    /**
     * 根据id删除优惠劵
     * @param id
     * @return
     */
    Object del(int id);

    /**
     * 根据ID获取优惠券
     * @param id
     * @return
     */
    Object getById(int id);

}
