package com.lyn521.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_coupon")
public class Coupon {
    private int id;
    private int userId;
    private Long startAt;
    private Long endAt;
    private Double money;
    @TableField(exist = false)
    private boolean state;
}
