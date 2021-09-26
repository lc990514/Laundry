package com.lyn521.controller;

import com.lyn521.pojo.SetMeal;
import com.lyn521.service.SetMealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/setMeal")
public class SetMealController {
    @Autowired
    private SetMealService setMealService;
    @Autowired(required = false)
    private HttpServletRequest request;

    @RequestMapping("/getAll")
    public Object getAll(int page, int limit) {
        return setMealService.getAll(page, limit);
    }

    @RequestMapping("/getAllBySId")
    public Object getAllBySId(int page, int limit) {
        int id = request.getIntHeader("id");
        return setMealService.getBySId(page, limit, id);
    }

    @RequestMapping("/del")
    public Object deleteById(String id) {
        return setMealService.delById(id);
    }

    @RequestMapping("/getById")
    public Object getById(int id) {
        return setMealService.getById(id);
    }

    @RequestMapping("/rest")
    public Object rest(SetMeal setMeal) {
        return setMealService.restSetMeal(setMeal);
    }

    @RequestMapping("/add")
    public Object add(SetMeal setMeal) {
        return setMealService.addSetMeal(setMeal);
    }

    @RequestMapping("/getType")
    public Object getType() {
        return setMealService.getType();
    }

    @GetMapping("/get/all")
    public Object getSetMealsAll(){
        return setMealService.getSetMealsAll();
    }

    @GetMapping("/getByType")
    public Object getByType(String type){
        return setMealService.getByType(type);
    }

    @PostMapping("/get/BySId")
    public Object getBySId(SetMeal setMeal){
        return setMealService.getSetMealsBySId(setMeal.getUserId());
    }
}

