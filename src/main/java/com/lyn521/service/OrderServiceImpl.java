package com.lyn521.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lyn521.mapper.*;
import com.lyn521.pojo.*;
import com.lyn521.pub.back.MsgCode;
import com.lyn521.pub.back.Result;
import com.lyn521.pub.tool.PagePo;
import com.lyn521.pub.tool.SHA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired(required = false)
    private OrderMapper orderMapper;
    @Autowired
    private SetMealMapper setMealMapper;
    @Autowired
    private SetMealService setMealService;
    @Autowired
    private CartService cartService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MerchantUserMapper merchantUserMapper;
    @Autowired
    private RecordsMapper recordsMapper;

    @Override
    public Object getAll(int page, int limit) {
        IPage<Order> page1 = new Page<>(page, limit);
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(
                Order::getId,
                Order::getUserId,
                Order::getSetMeal,
                Order::getTime,
                Order::getMoney,
                Order::getAddress,
                Order::isState,
                Order::getSuserId,
                Order::getStates
        );
        page1 = orderMapper.selectPage(page1, queryWrapper);
        PagePo pagePo = new PagePo((int) page1.getTotal(), page1.getRecords());
        return Result.success(pagePo);
    }

    @Override
    public Object getOrderById(int id) {
        Order order = orderMapper.selectById(id);
        if (order == null) {
            return Result.error(MsgCode.SERVER_ERROR);
        }
        return Result.success(order);
    }

    @Override
    public Object delOrderById(String id) {
        String[] ids = id.split(";");
        int row = 0;
        for (String s : ids) {
            row += orderMapper.deleteById(s);
        }
        if (row == 0) {
            return Result.error(MsgCode.SERVER_ERROR);
        }
        return Result.success(row);
    }

    @Override
    public Object addOrder(Order order) {
        JSONArray objects = JSONObject.parseArray(order.getSetMeal());
        List<Integer> setMealList = new ArrayList<>();
        int row = orderMapper.insert(order);
        if (row == 0) {
            return Result.error(MsgCode.SERVER_ERROR);
        }
        return Result.success(row);
    }

    @Override
    public Object restOrder(Order order) {
        int row = orderMapper.updateById(order);
        if (row == 0) {
            return Result.error(MsgCode.SERVER_ERROR);
        }
        return Result.success(row);
    }

    @Override
    public Object restState(int id, Boolean state) {
        LambdaUpdateWrapper<Order> updateWrapper = new LambdaUpdateWrapper<>();
        System.out.println(state);
        updateWrapper.set(Order::isState, state).eq(Order::getId, id);
        int row = orderMapper.update(null, updateWrapper);
        if (row == 0) {
            return Result.error(MsgCode.SERVER_ERROR);
        }
        return Result.success(null);
    }

    @Override
    public Object getAllBySId(int page, int limit, int id) {
        IPage<Order> page1 = new Page<>(page, limit);
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(
                Order::getId,
                Order::getUserId,
                Order::getSetMeal,
                Order::getTime,
                Order::getMoney,
                Order::getAddress,
                Order::isState,
                Order::getSuserId,
                Order::getStates
        ).eq(Order::getSuserId, id);
        page1 = orderMapper.selectPage(page1, queryWrapper);
        PagePo pagePo = new PagePo((int) page1.getTotal(), page1.getRecords());
        return Result.success(pagePo);
    }

    @Override
    public Object setNewOrder(Order order) throws JsonProcessingException {
        JSONArray objects = JSONObject.parseArray(order.getSetMeal());
        List<Integer> setMealList = new ArrayList<>();
        List<Cart> carts = new ArrayList<>();
        System.out.println(order);
        for (int i = 0; i < objects.size(); i++) {
            setMealList.add((Integer) objects.getJSONObject(i).get("gid"));
            Cart cart = new Cart();
            cart.setId((Integer) objects.getJSONObject(i).get("id"));
            cart.setNum((Integer) objects.getJSONObject(i).get("num"));
            cart.setGid((Integer) objects.getJSONObject(i).get("gid"));
            carts.add(cart);
        }
        List<SetMeal> setMealsList1 = setMealService.getSetMealsById(setMealList);
        Double money = 0.0;
        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        Records records = new Records();
        for (int i = 0; i < carts.size(); i++) {
            Order order1 = order;
            Cart cart = carts.get(i);
            SetMeal setMeal = null;
            for (SetMeal setMeal1 : setMealsList1) {
                if (setMeal1.getId() == cart.getGid()) {
                    setMeal = setMeal1;
                    setMeal.setNum(cart.getNum());
                }
            }
            if (setMeal == null) {
                return Result.error(MsgCode.SetMeal_NULL);
            }
            money += setMeal.getPrice() * cart.getNum();
            json = mapper.writeValueAsString(setMeal);
            order1.setSetMeal(json);
            order1.setSuserId(setMeal.getUserId());
            order1.setMoney(money);
            order1.setState(true);
            //设置订单进度
            order1.setStates(1);

            int row = orderMapper.insert(order1);
            if (row <= 0) {
                return Result.error(MsgCode.SERVER_ERROR);
            }
            //添加事件
            records.setMessages("用户下单");
            records.setUserId(order1.getSuserId());
            records.setTime(SHA.getNowTime());
            int insert = recordsMapper.insert(records);
            if (insert<=0){
                return Result.error(MsgCode.SERVER_ERROR);
            }
            //商户经验金额变更
            LambdaQueryWrapper<MerchantUser> merchantUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
            merchantUserLambdaQueryWrapper
                    .select(MerchantUser::getGrade, MerchantUser::getExperience,MerchantUser::getBalance)
                    .eq(MerchantUser::getId, order1.getSuserId());
            MerchantUser merchantUser = merchantUserMapper.selectOne(merchantUserLambdaQueryWrapper);
            if (merchantUser == null) {
                return Result.error(MsgCode.SERVER_ERROR);
            }
            int grade = (merchantUser.getExperience() + 1) / 100;
            if (grade > 5) {
                grade = 5;
            }
            merchantUser.setGrade(grade);
            LambdaUpdateWrapper<MerchantUser> merchantUserLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            merchantUserLambdaUpdateWrapper
                    .set(MerchantUser::getExperience,merchantUser.getExperience()+1)
                    .set(MerchantUser::getGrade,merchantUser.getGrade())
                    .set(MerchantUser::getBalance,merchantUser.getBalance()+money)
                    .eq(MerchantUser::getId,order1.getSuserId());
            row = merchantUserMapper.update(null, merchantUserLambdaUpdateWrapper);
            if (row<=0){
                return Result.error(MsgCode.SERVER_ERROR);
            }
            money = 0.0;

        }
        //用户经验变更
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.select(User::getExperience, User::getGrade).eq(User::getId, order.getUserId());
        User user = userMapper.selectOne(userLambdaQueryWrapper);
        if (user == null) {
            return Result.error(MsgCode.SERVER_ERROR);
        }
        int grade = (user.getExperience() + setMealList.size()) / 100;
        if (grade > 5) {
            grade = 5;
        }
        user.setGrade(grade);
        LambdaUpdateWrapper<User> userLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        userLambdaUpdateWrapper
                .set(User::getExperience, user.getExperience() + setMealList.size())
                .set(User::getGrade, user.getGrade())
                .eq(User::getId, order.getUserId());
        int row = userMapper.update(null, userLambdaUpdateWrapper);
        if (row <= 0) {
            return Result.error(MsgCode.SERVER_ERROR);
        }
        List<Integer> cartId=new ArrayList<>();
        for (Cart cart : carts) {
            cartId.add(cart.getId());
        }
        //删除购物车内容
        cartService.removeCartById(cartId);
        return Result.success(row);
    }

    @Override
    public Object getAllOrder(int id) {
        LambdaQueryWrapper<Order> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper
                .select(Order::getId,
                        Order::getSetMeal,
                        Order::getTime,
                        Order::getMoney,
                        Order::isState,
                        Order::getUserId,
                        Order::getSuserId,
                        Order::getStates)
                .orderByDesc(Order::getTime)
                .eq(Order::getUserId,id);
        List<Order> orders=orderMapper.selectList(queryWrapper);
        return Result.success(orders);
    }

    @Override
    public Object removeOrder(int id) {
        LambdaUpdateWrapper<Order> orderLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        orderLambdaUpdateWrapper.set(Order::isState,false).eq(Order::getId,id);
        int row = orderMapper.update(null, orderLambdaUpdateWrapper);
        if (row<=0){
            return Result.error(MsgCode.SERVER_ERROR);
        }
        //获取用户商户ID
        LambdaQueryWrapper<Order> orderLambdaQueryWrapper = new LambdaQueryWrapper<>();
        orderLambdaQueryWrapper
                .select(Order::getUserId,Order::getSuserId)
                .eq(Order::getId,id);
        Order order = orderMapper.selectOne(orderLambdaQueryWrapper);
        if (order==null){
            return Result.error(MsgCode.SERVER_ERROR);
        }
        //添加事件
        Records records = new Records();
        records.setMessages("用户退单");
        records.setUserId(order.getSuserId());
        records.setTime(SHA.getNowTime());
        int insert = recordsMapper.insert(records);
        if (insert<=0){
            return Result.error(MsgCode.SERVER_ERROR);
        }
        //用户经验
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.select(User::getExperience, User::getGrade).eq(User::getId, order.getUserId());
        User user = userMapper.selectOne(userLambdaQueryWrapper);
        if (user == null) {
            return Result.error(MsgCode.SERVER_ERROR);
        }
        int grade = (user.getExperience() - 1) / 100;
        if (grade > 5) {
            grade = 5;
        }
        user.setGrade(grade);
        LambdaUpdateWrapper<User> userLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        userLambdaUpdateWrapper
                .set(User::getExperience, user.getExperience() - 1)
                .set(User::getGrade, user.getGrade())
                .eq(User::getId, order.getUserId());
        row = userMapper.update(null, userLambdaUpdateWrapper);
        if (row <= 0) {
            return Result.error(MsgCode.SERVER_ERROR);
        }
        //商户经验
        LambdaQueryWrapper<MerchantUser> merchantUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        merchantUserLambdaQueryWrapper
                .select(MerchantUser::getGrade, MerchantUser::getExperience)
                .eq(MerchantUser::getId, order.getSuserId());
        MerchantUser merchantUser = merchantUserMapper.selectOne(merchantUserLambdaQueryWrapper);
        if (merchantUser == null) {
            return Result.error(MsgCode.SERVER_ERROR);
        }
        grade = (merchantUser.getExperience() - 1) / 100;
        if (grade > 5) {
            grade = 5;
        }
        merchantUser.setGrade(grade);
        LambdaUpdateWrapper<MerchantUser> merchantUserLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        merchantUserLambdaUpdateWrapper
                .set(MerchantUser::getExperience,merchantUser.getExperience()-1)
                .set(MerchantUser::getGrade,merchantUser.getGrade())
                .eq(MerchantUser::getId,order.getSuserId());
        row = merchantUserMapper.update(null, merchantUserLambdaUpdateWrapper);
        if (row<=0){
            return Result.error(MsgCode.SERVER_ERROR);
        }
        return Result.success(1);
    }

    @Override
    public Object setStates(int id,int states) {
        LambdaUpdateWrapper<Order> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.set(Order::getStates,states).eq(Order::getId,id);
        int update = orderMapper.update(null, lambdaUpdateWrapper);
        if (update<=0){
            return Result.error(MsgCode.SERVER_ERROR);
        }
        return Result.success(1);
    }
}
