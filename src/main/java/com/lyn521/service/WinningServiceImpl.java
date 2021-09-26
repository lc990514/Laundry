package com.lyn521.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.lyn521.mapper.CouponMapper;
import com.lyn521.mapper.UserMapper;
import com.lyn521.mapper.WinningMapper;
import com.lyn521.pojo.Coupon;
import com.lyn521.pojo.User;
import com.lyn521.pojo.Winning;
import com.lyn521.pub.back.MsgCode;
import com.lyn521.pub.back.Result;
import com.lyn521.pub.tool.SHA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class WinningServiceImpl implements WinningService {
    @Autowired
    private WinningMapper winningMapper;
    @Autowired
    private CouponMapper couponMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public Object getAll(int id) {
        LambdaQueryWrapper<Winning> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Winning::getUserId, id);
        List<Winning> winnings = winningMapper.selectList(lambdaQueryWrapper);
        return Result.success(winnings);
    }

    @Override
    public Object intoWinning(int id) {
        // 经验加1 1元优惠券 经验加1 谢谢参与 2元优惠券 经验加5 经验加1 谢谢参与 8个内容
        String[] str = {"1点经验", "1元优惠券", "1点经验", "谢谢参与", "2元优惠券", "5点经验", "1点经验", "谢谢参与"};
        //获取随机数
        int random = (int) (8 + Math.random() * 9);
        //抽中内容的index
        int index = random % 8;
        //排除是否为谢谢参与
        if (index == 3 || index == 7) {
            return Result.success(random);
        }
        //添加中奖记录
        Winning winning = new Winning();
        winning.setMessage(str[index]);
        winning.setUserId(id);
        winning.setTime(SHA.getNowTime());
        int insert = winningMapper.insert(winning);
        if (insert <= 0) {
            return Result.error(MsgCode.SERVER_ERROR);
        }
        //判断抽中的是经验还是优惠券 state  true为优惠券 false为经验 默认为true
        boolean state = true;
        int experience = 0;
        double value = 0;
        switch (index) {
            case 0:
            case 2:
            case 6:
                state = false;
                experience = 1;
                break;
            case 1:
                value = 1;
                break;
            case 4:
                value = 2;
                break;
            case 5:
                state = false;
                experience = 5;
                break;
        }
        //进行兑奖
        if (state){
            //添加优惠券
            Coupon coupon = new Coupon();
            coupon.setUserId(id);
            Long time = getUnixTime(new Date());
            coupon.setStartAt(time);
            coupon.setEndAt(time+2592000);
            coupon.setMoney(value);
            int row = couponMapper.insert(coupon);
            if (row<=0){
                return Result.error(MsgCode.SERVER_ERROR);
            }
        }else {
            //添加经验
            LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
            userLambdaQueryWrapper.select(User::getExperience, User::getGrade).eq(User::getId, id);
            User user = userMapper.selectOne(userLambdaQueryWrapper);
            if (user == null) {
                return Result.error(MsgCode.SERVER_ERROR);
            }
            int grade = (user.getExperience() + experience) / 100;
            if (grade > 5) {
                grade = 5;
            }
            user.setGrade(grade);
            LambdaUpdateWrapper<User> userLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            userLambdaUpdateWrapper
                    .set(User::getExperience, user.getExperience() + experience)
                    .set(User::getGrade, user.getGrade())
                    .eq(User::getId, id);
            int row = userMapper.update(null, userLambdaUpdateWrapper);
            if (row <= 0) {
                return Result.error(MsgCode.SERVER_ERROR);
            }
        }
        return Result.success(random);
    }

    //获取时间戳
    public static long getUnixTime(Date date) {
        if (null == date) {
            return 0;
        }
        return date.getTime() / 1000;
    }
}
