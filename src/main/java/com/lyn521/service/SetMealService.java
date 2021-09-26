package com.lyn521.service;

import com.lyn521.pojo.SetMeal;

import java.util.List;

public interface SetMealService {
    /**
     * 获取所有套餐
     * @param page
     * @param limit
     * @return
     */
    Object getAll(int page, int limit);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    Object delById(String id);

    /**
     * 根据id获取
     * @param id
     * @return
     */
    Object getById(int id);

    /**
     * 更新套餐
     * @param setMeal
     * @return
     */
    Object restSetMeal(SetMeal setMeal);

    /**
     * 添加套餐
     * @param setMeal
     * @return
     */
    Object addSetMeal(SetMeal setMeal);

    /**
     * 获取套餐分类
     * @return
     */
    Object getType();

    /**
     * 根据商户id获取
     * @param page
     * @param limit
     * @param id
     * @return
     */
    Object getBySId(int page,int limit,int id);

    /**
     * 获取所有商品，不分页
     * @return
     */
    Object getSetMealsAll();

    /**
     * 根据类型获取
     * @return
     */
    Object getByType(String type);

    /**
     * 根据id获取所有
     * @param list
     * @return
     */
    List<SetMeal> getSetMealsById(List<Integer> list);

    Object getSetMealsBySId(int id);
}
