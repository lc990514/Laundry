package com.lyn521.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyn521.mapper.SetMealMapper;
import com.lyn521.pojo.SetMeal;
import com.lyn521.pub.back.MsgCode;
import com.lyn521.pub.back.Result;
import com.lyn521.pub.tool.PagePo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@Repository
public class SetMealServiceImpl implements SetMealService {
    @Autowired()
    private SetMealMapper setMealMapper;

    @Override
    public Object getAll(int page, int limit) {
        IPage<SetMeal> page1 = new Page<>(page, limit);
        LambdaQueryWrapper<SetMeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(
                SetMeal::getId,
                SetMeal::getSname,
                SetMeal::getType,
                SetMeal::getPrice,
                SetMeal::getUserId,
                SetMeal::getTests,
                SetMeal::getImgs
        );
        page1 = setMealMapper.selectPage(page1, queryWrapper);
        PagePo pagePo = new PagePo((int) page1.getTotal(), page1.getRecords());
        return Result.success(pagePo);
    }

    @Override
    public Object delById(String id) {
        String[] uid = id.split(";");
        int row = 0;
        for (String s : uid) {
            row += setMealMapper.deleteById(s);
        }
        if (row > 0) {
            return Result.success(row);
        }
        return Result.error(MsgCode.SERVER_ERROR);
    }

    @Override
    public Object getById(int id) {
        SetMeal setMeal = setMealMapper.selectById(id);
        if (setMeal == null) {
            return Result.error(MsgCode.SERVER_ERROR);
        }
        return Result.success(setMeal);
    }

    @Override
    public Object restSetMeal(SetMeal setMeal) {
        int row = setMealMapper.updateById(setMeal);
        if (row > 0) {
            return Result.success(null);
        }
        return Result.error(MsgCode.SERVER_ERROR);
    }

    @Override
    public Object addSetMeal(SetMeal setMeal) {
        int row = setMealMapper.insert(setMeal);
        if (row > 0) {
            return Result.success(null);
        }
        return Result.error(MsgCode.SERVER_ERROR);
    }

    @Override
    public Object getType() {
        LambdaQueryWrapper<SetMeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(SetMeal::getType);
        List<SetMeal> setMeals = setMealMapper.selectList(queryWrapper);
        List<String> type =new ArrayList<>();
        for (int i = 0; i < setMeals.size(); i++) {
            String[] types = setMeals.get(i).getType().split(";");
            for (int i1 = 0; i1 < types.length; i1++) {
                type.add(types[i1]);
            }
        }
        HashSet set=new HashSet(type);
        type.clear();
        type.addAll(set);
        return Result.success(type);
    }

    @Override
    public Object getBySId(int page,int limit,int id) {
        IPage<SetMeal> page1 = new Page<>(page, limit);
        LambdaQueryWrapper<SetMeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(
                SetMeal::getId,
                SetMeal::getSname,
                SetMeal::getType,
                SetMeal::getPrice,
                SetMeal::getUserId,
                SetMeal::getTests
        ).eq(SetMeal::getUserId,id);
        page1 = setMealMapper.selectPage(page1,queryWrapper);
        PagePo pagePo = new PagePo((int) page1.getTotal(),page1.getRecords());
        return Result.success(pagePo);
    }

    @Override
    public Object getSetMealsAll() {
        LambdaQueryWrapper<SetMeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(
                SetMeal::getId,
                SetMeal::getSname,
                SetMeal::getType,
                SetMeal::getPrice,
                SetMeal::getUserId,
                SetMeal::getTests,
                SetMeal::getImgs
        );
        List<SetMeal> setMeals = setMealMapper.selectList(queryWrapper);
        return Result.success(setMeals);
    }

    @Override
    public Object getByType(String type) {
        LambdaQueryWrapper<SetMeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(
                SetMeal::getId,
                SetMeal::getSname,
                SetMeal::getType,
                SetMeal::getPrice,
                SetMeal::getUserId,
                SetMeal::getTests,
                SetMeal::getImgs
        ).or().like(SetMeal::getType,type);
        List<SetMeal> setMeals = setMealMapper.selectList(lambdaQueryWrapper);
        return Result.success(setMeals);
    }

    @Override
    public List<SetMeal> getSetMealsById(List<Integer> list) {
        LambdaQueryWrapper<SetMeal> setMealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setMealLambdaQueryWrapper
                .in(SetMeal::getId,list)
                .select(
                        SetMeal::getId,
                        SetMeal::getSname,
                        SetMeal::getPrice,
                        SetMeal::getType,
                        SetMeal::getTests,
                        SetMeal::getUserId,
                        SetMeal::getImgs);
        return setMealMapper.selectList(setMealLambdaQueryWrapper);
    }

    @Override
    public Object getSetMealsBySId(int id) {
        LambdaQueryWrapper<SetMeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(
                SetMeal::getId,
                SetMeal::getSname,
                SetMeal::getType,
                SetMeal::getPrice,
                SetMeal::getUserId,
                SetMeal::getTests,
                SetMeal::getImgs
        ).eq(SetMeal::getUserId,id);
        List<SetMeal> setMeals = setMealMapper.selectList(lambdaQueryWrapper);
        return Result.success(setMeals);
    }
}
