package com.lyn521.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_order")
public class Order {
    private int id;
    private int userId;
    private String time;
    private String setMeal;
    private Double money;
    private String address;
    private boolean state;
    private int suserId;
    private int states;
}
