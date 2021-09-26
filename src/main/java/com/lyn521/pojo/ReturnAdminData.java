package com.lyn521.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * admin
 * 用于封装传给后台画图所用的数据
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReturnAdminData {
    private int users;
    private int merchants;
    private int setMeals;
    private int orders;
    private int comments;
}
