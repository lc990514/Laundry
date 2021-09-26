package com.lyn521.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("t_cart")
@Data
public class Cart {
    private int id;
    private int uid;
    private int gid;
    private int num;
}
