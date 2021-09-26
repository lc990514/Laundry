package com.lyn521.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyn521.mapper.CommentsMapper;
import com.lyn521.mapper.MerchantUserMapper;
import com.lyn521.mapper.OrderMapper;
import com.lyn521.mapper.SetMealMapper;
import com.lyn521.pojo.*;
import com.lyn521.pub.back.MsgCode;
import com.lyn521.pub.back.Result;
import com.lyn521.pub.tool.PagePo;
import com.lyn521.pub.tool.SHA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
public class MerchantUserServiceImpl implements MerchantUserService {

    @Autowired
    private MerchantUserMapper userMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private CommentsMapper commentsMapper;
    @Autowired
    private SetMealMapper setMealMapper;

    @Override
    public Object getSUserAll(int page, int limit) {
        IPage<MerchantUser> pge = new Page<>(page, limit);
        LambdaQueryWrapper<MerchantUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(
                MerchantUser::getId,
                MerchantUser::getUsername,
                MerchantUser::getTime,
                MerchantUser::getAddress,
                MerchantUser::getGrade,
                MerchantUser::getUname,
                MerchantUser::getExperience,
                MerchantUser::getBalance
        );
        pge = userMapper.selectPage(pge, queryWrapper);
        PagePo pagePo = new PagePo((int) pge.getTotal(), pge.getRecords());
        return Result.success(pagePo);
    }

    @Override
    public Object delSUserById(String id) {
        String[] uid = id.split(";");
        int row = 0;
        for (int i = 0; i < uid.length; i++) {
            row += userMapper.deleteById(uid[i]);
        }
        if (row > 0) {
            return Result.success(row);
        }
        return Result.error(MsgCode.SERVER_ERROR);
    }

    @Override
    public Object getSUerById(int id) {
        MerchantUser merchantUser = userMapper.selectById(id);
        if (merchantUser == null) {
            return Result.error(MsgCode.SERVER_ERROR);
        }
        return Result.success(merchantUser);
    }

    @Override
    public Object restSUser(MerchantUser user) {
        if (user.getPassword().equals("") ||user.getPassword()==null){
            LambdaQueryWrapper<MerchantUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.select(MerchantUser::getPassword).eq(MerchantUser::getId,user.getId());
            MerchantUser merchantUser = userMapper.selectOne(lambdaQueryWrapper);
            user.setPassword(merchantUser.getPassword());
        }else {
            user.setPassword(SHA.md5(user.getPassword()));
        }
        int grade = user.getExperience() / 100;
        if (grade > 5) {
            grade = 5;
        }
        user.setGrade(grade);
        int row = userMapper.updateById(user);
        if (row > 0) {
            return Result.success(null);
        }
        return Result.error(MsgCode.SERVER_ERROR);
    }

    @Override
    public Object addSUser(MerchantUser user) {
        user.setPassword(SHA.md5(user.getPassword()));
        int grade = user.getExperience() / 100;
        if (grade > 5) {
            grade = 5;
        }
        user.setGrade(grade);
        int row = userMapper.insert(user);
        if (row > 0) {
            return Result.success(null);
        }
        return Result.error(MsgCode.SERVER_ERROR);
    }

    @Override
    public Object loginSUser(MerchantUser user) {
        user.setPassword(SHA.md5(user.getPassword()));
        LambdaQueryWrapper<MerchantUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(MerchantUser::getUsername, user.getUsername())
                .eq(MerchantUser::getPassword, user.getPassword());
        user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            return Result.error(MsgCode.USER_PASSWORD_ERROR);
        }
        String token = SHA.getDataMD5();
        LambdaUpdateWrapper<MerchantUser> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper
                .set(MerchantUser::getToken, token)
                .set(MerchantUser::getTime, SHA.getNowTime())
                .eq(MerchantUser::getId, user.getId());
        int row = userMapper.update(null, updateWrapper);
        if (row > 0) {
            user.setToken(token);
            user.setPassword(null);
            return Result.success(user);
        }
        return Result.error(MsgCode.SERVER_ERROR);
    }

    @Override
    public Object registerSUser(MerchantUser user) {
        user.setPassword(SHA.md5(user.getPassword()));
        user.setBalance(0.0);
        user.setExperience(0);
        user.setGrade(0);
        int row = userMapper.insert(user);
        if (row>0){
            return Result.success(row);
        }
        return Result.error(MsgCode.SERVER_ERROR);
    }

    @Override
    public Object token(MerchantUser user) {
        LambdaQueryWrapper<MerchantUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(MerchantUser::getId, user.getId())
                .eq(MerchantUser::getToken, user.getToken());
        MerchantUser merchantUser = userMapper.selectOne(queryWrapper);
        if (merchantUser == null) {
            return Result.error(MsgCode.SESSION_ERROR);
        }
        Instant inst1 = Instant.now();
        Instant inst2 = SHA.toDate(merchantUser.getTime()).toInstant();
        long seconds = 7200 - Duration.between(inst2, inst1).getSeconds();
        return Result.success(seconds);
    }

    @Override
    public Object restMsg(MerchantUser user) {
        int row = userMapper.updateById(user);
        if (row > 0) {
            return Result.success(row);
        }
        return Result.error(MsgCode.SERVER_ERROR);
    }

    @Override
    public Object getData(int id) {
        ReturnData data = new ReturnData();
        LambdaQueryWrapper<Order> orderLambdaQueryWrapper = new LambdaQueryWrapper<>();
        orderLambdaQueryWrapper.eq(Order::getSuserId, id);
        List<Order> orders = orderMapper.selectList(orderLambdaQueryWrapper);
        Double money = 0.0;
        if (orders != null && !orders.isEmpty()) {
            for (int i = 0; i < orders.size(); i++) {
                if (orders.get(i).getMoney() != null) {
                    money += orders.get(i).getMoney();
                }
            }
        }
        data.setMoney(money);
        data.setOrders(orderMapper.selectCount(orderLambdaQueryWrapper));
        LambdaQueryWrapper<Comments> commentsLambdaQueryWrapper = new LambdaQueryWrapper<>();
        commentsLambdaQueryWrapper.eq(Comments::getSuserId, id);
        Integer comments = commentsMapper.selectCount(commentsLambdaQueryWrapper);
        data.setComments(comments);
        LambdaQueryWrapper<SetMeal> setMealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setMealLambdaQueryWrapper.eq(SetMeal::getUserId, id);
        Integer setMeals = setMealMapper.selectCount(setMealLambdaQueryWrapper);
        data.setSetMeals(setMeals);
        System.out.println(data);
        return Result.success(data);
    }

    @Override
    public Object restPassword(MerchantUser user) {
        if (user.getPassword().equals("")||user.getPassword()==null){
            return Result.success(null);
        }
        user.setPassword(SHA.md5(user.getPassword()));
        LambdaUpdateWrapper<MerchantUser> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.set(MerchantUser::getPassword,user.getPassword()).eq(MerchantUser::getId,user.getId());
        int update = userMapper.update(null, lambdaUpdateWrapper);
        if (update<=0){
            return Result.error(MsgCode.SERVER_ERROR);
        }
        return Result.success(null);
    }

    @Override
    public Object getAll() {
        LambdaQueryWrapper<MerchantUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(
                MerchantUser::getId,
                MerchantUser::getAddress,
                MerchantUser::getGrade,
                MerchantUser::getUname,
                MerchantUser::getIntroduce,
                MerchantUser::getPortrait
        );
        List<MerchantUser> merchantUsers = userMapper.selectList(lambdaQueryWrapper);
        return Result.success(merchantUsers);
    }
}
