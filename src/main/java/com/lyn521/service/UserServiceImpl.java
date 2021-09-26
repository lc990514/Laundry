package com.lyn521.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyn521.mapper.UserMapper;
import com.lyn521.pojo.User;
import com.lyn521.pub.back.MsgCode;
import com.lyn521.pub.back.Result;
import com.lyn521.pub.tool.PagePo;
import com.lyn521.pub.tool.SHA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired(required = false)
    private UserMapper userMapper;

    @Override
    public Object getYUserAll(int page, int limit) {
        IPage<User> page1 = new Page<>(page, limit);
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(
                User::getId,
                User::getUsername,
                User::getTime,
                User::getAddress,
                User::getGrade,
                User::getExperience,
                User::getUname
        );
        page1 = userMapper.selectPage(page1, queryWrapper);
        PagePo pagePo = new PagePo((int) page1.getTotal(), page1.getRecords());
        return Result.success(pagePo);
    }

    @Override
    public Object delYUserById(String id) {
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
    public Object getYUerById(int id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            return Result.error(MsgCode.SERVER_ERROR);
        }
        return Result.success(user);
    }

    @Override
    public Object restYUser(User user) {
        if (user.getPassword()==null||user.getPassword().equals("")){
            LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.select(User::getPassword).eq(User::getId,user.getId());
            User user1 = userMapper.selectOne(lambdaQueryWrapper);
            user.setPassword(user1.getPassword());
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
            return Result.success(1);
        }
        return Result.error(MsgCode.SERVER_ERROR);
    }

    @Override
    public Object addYUser(User user) {
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
    public Object token(User user) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(User::getId, user.getId())
                .eq(User::getToken, user.getToken())
                .select(User::getId, User::getToken, User::getUsername);
        User user1 = userMapper.selectOne(queryWrapper);
        if (user1 == null) {
            return Result.error(MsgCode.SESSION_ERROR);
        }
        return Result.success(user1);
    }

    @Override
    public Object login(User user) {
        user.setPassword(SHA.md5(user.getPassword()));
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper
                .eq(User::getUsername,user.getUsername())
                .eq(User::getPassword,user.getPassword());
        user = userMapper.selectOne(lambdaQueryWrapper);
        if (user==null){
            return Result.error(MsgCode.USER_PASSWORD_ERROR);
        }
        String token = SHA.getDataMD5();
        LambdaUpdateWrapper<User> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper
                .set(User::getToken,token)
                .set(User::getTime,SHA.getNowTime())
                .eq(User::getId,user.getId());
        int row = userMapper.update(null,lambdaUpdateWrapper);
        if (row > 0) {
            user.setToken(token);
            user.setPassword(null);
            return Result.success(user);
        }
        return Result.error(MsgCode.SERVER_ERROR);
    }

    @Override
    public Object register(User user) {
        user.setPassword(SHA.md5(user.getPassword()));
        user.setGrade(0);
        user.setExperience(0);
        int row = userMapper.insert(user);
        if (row>0){
            return Result.success(1);
        }
        return Result.error(MsgCode.SESSION_ERROR);
    }
}
