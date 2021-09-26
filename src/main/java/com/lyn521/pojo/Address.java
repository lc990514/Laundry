package com.lyn521.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("t_address")
@Data
public class Address {
    private int id;
    private int uid;
    private String address;
    private boolean chief;
    private String name;
    private String tel;
}
