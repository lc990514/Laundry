package com.lyn521.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("set_meal")
public class SetMeal {
    private int id;
    private String sname;
    private Double price;
    private String type;
    private String tests;
    private int userId;
    private String imgs;
    @TableField(exist = false)
    private int num;
}
