package com.lyn521.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.lyn521.mapper.*;
import com.lyn521.pojo.ReturnAdminData;
import com.lyn521.pojo.SuperUser;
import com.lyn521.pojo.User;
import com.lyn521.pub.back.MsgCode;
import com.lyn521.pub.back.Result;
import com.lyn521.pub.tool.SHA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
@Repository
public class SuperUserServiceImpl implements SuperUserService {

    @Autowired
    private SuperUserMapper superUserMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MerchantUserMapper merchantUserMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private CommentsMapper commentsMapper;
    @Autowired
    private SetMealMapper setMealMapper;

    @Override
    public Object login(SuperUser user) {
        user.setPassword(SHA.md5(user.getPassword()));
        LambdaQueryWrapper<SuperUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(SuperUser::getUsername, user.getUsername())
                .eq(SuperUser::getPassword, user.getPassword());
        user = superUserMapper.selectOne(queryWrapper);
        if (user == null) {
            return Result.error(MsgCode.USER_PASSWORD_ERROR);
        }
        String token = SHA.getDataMD5();
        LambdaUpdateWrapper<SuperUser> superUserLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        superUserLambdaUpdateWrapper
                .set(SuperUser::getToken, token)
                .set(SuperUser::getTime, SHA.getNowTime())
                .eq(SuperUser::getId, user.getId());
        int row = superUserMapper.update(null, superUserLambdaUpdateWrapper);
        if (row > 0) {
            user.setToken(token);
            user.setPassword(null);
            return Result.success(user);
        }
        return Result.error(MsgCode.SERVER_ERROR);
    }

    @Override
    public Object token(SuperUser user) {
        LambdaQueryWrapper<SuperUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper
                .eq(SuperUser::getId, user.getId())
                .eq(SuperUser::getToken, user.getToken());
        SuperUser superUser = superUserMapper.selectOne(lambdaQueryWrapper);
        if (superUser == null) {
            return Result.error(MsgCode.SESSION_ERROR);
        }
        Instant inst1 = Instant.now();
        Instant inst2 = SHA.toDate(superUser.getTime()).toInstant();
        long seconds = 7200 - Duration.between(inst2, inst1).getSeconds();
        return Result.success(seconds);
    }

    @Override
    public Object getUserById(int id) {
        SuperUser superUser = superUserMapper.selectById(id);
        if (superUser == null) {
            return Result.error(MsgCode.SESSION_ERROR);
        }
        return Result.success(superUser);
    }

    @Override
    public Object restUserById(SuperUser user) {
        int row = superUserMapper.updateById(user);
        if (row > 0) {
            return Result.success(null);
        }
        return Result.error(MsgCode.DATA_NULL);
    }

    @Override
    public Object getChartData() {
        Integer users = userMapper.selectCount(null);
        Integer merchants = merchantUserMapper.selectCount(null);
        Integer setMeals = setMealMapper.selectCount(null);
        Integer orders = orderMapper.selectCount(null);
        Integer comments = commentsMapper.selectCount(null);
        ReturnAdminData data = new ReturnAdminData(users,merchants,setMeals,orders,comments);
        return Result.success(data);
    }

    @Override
    public Object restPassword(SuperUser user) {
        if (user.getPassword().equals("")||user.getPassword()==null){
            return Result.success(null);
        }
        user.setPassword(SHA.md5(user.getPassword()));
        LambdaUpdateWrapper<SuperUser> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.set(SuperUser::getPassword,user.getPassword()).eq(SuperUser::getId,user.getId());
        int update = superUserMapper.update(null, lambdaUpdateWrapper);
        if (update<=0){
            return Result.error(MsgCode.SERVER_ERROR);
        }
        return Result.success(null);
    }


}
