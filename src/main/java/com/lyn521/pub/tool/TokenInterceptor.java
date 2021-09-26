package com.lyn521.pub.tool;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lyn521.mapper.MerchantUserMapper;
import com.lyn521.mapper.SuperUserMapper;
import com.lyn521.mapper.UserMapper;
import com.lyn521.pojo.MerchantUser;
import com.lyn521.pojo.SuperUser;
import com.lyn521.pojo.User;
import com.lyn521.pub.back.MsgCode;
import com.lyn521.pub.back.Result;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.Instant;

public class TokenInterceptor implements HandlerInterceptor {

    SuperUserMapper superUserMapper;
    MerchantUserMapper merchantUserMapper;
    UserMapper userMapper;

    public TokenInterceptor(SuperUserMapper superUserMapper,MerchantUserMapper merchantUserMapper,UserMapper userMapper) {
        this.superUserMapper = superUserMapper;
        this.merchantUserMapper = merchantUserMapper;
        this.userMapper = userMapper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        int id = request.getIntHeader("id");
        String identity = request.getHeader("identity");
        System.out.println("----------------------------"+request.getServletPath());
        System.out.println(identity);
        if (token==null || id==0){
            sendJSON(response,JSON.toJSONString(Result.error(MsgCode.DATA_NULL)));
            return false;
        }
        Instant inst1 = Instant.now();
        Instant inst2 = null;
        if (identity == null){
            LambdaQueryWrapper<SuperUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(SuperUser::getId,id)
                    .eq(SuperUser::getToken,token);
            SuperUser superUser = superUserMapper.selectOne(lambdaQueryWrapper);
            if (superUser==null){
                sendJSON(response, JSON.toJSONString(Result.error(MsgCode.SESSION_ERROR)));
                return false;
            }
            inst2 = SHA.toDate(superUser.getTime()).toInstant();
        }else if (identity.equals("merchant")){
            LambdaQueryWrapper<MerchantUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(MerchantUser::getId,id)
                    .eq(MerchantUser::getToken,token);
            MerchantUser merchantUser = merchantUserMapper.selectOne(lambdaQueryWrapper);
            if (merchantUser==null){
                sendJSON(response, JSON.toJSONString(Result.error(MsgCode.SESSION_ERROR)));
                return false;
            }
            inst2 = SHA.toDate(merchantUser.getTime()).toInstant();
        }else if (identity.equals("user")){
            LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper
                    .eq(User::getId,id)
                    .eq(User::getToken,token);
            User user = userMapper.selectOne(lambdaQueryWrapper);
            if (user==null){
                sendJSON(response, JSON.toJSONString(Result.error(MsgCode.SESSION_ERROR)));
                return false;
            }
            inst2 = SHA.toDate(user.getTime()).toInstant();
        }
        long seconds=7200- Duration.between(inst2,inst1).getSeconds();
        if (seconds<=0){
            sendJSON(response, JSON.toJSONString(Result.error(MsgCode.SESSION_ERROR)));
            return false;
        }
        return true;
    }

    public void sendJSON(HttpServletResponse response,String message){
        PrintWriter writer = null;
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("json/html; charset=utf-8");
            writer = response.getWriter();
            writer.print(message);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (writer!=null){
                writer.close();
            }
        }
    }
}
