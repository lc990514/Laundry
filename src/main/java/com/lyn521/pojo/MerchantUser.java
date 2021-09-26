package com.lyn521.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_merchant_user")
public class MerchantUser {
    private int id;
    private String username;
    private String password;
    private String portrait;
    private String token;
    private String time;
    private String introduce;
    private String address;
    private int grade;
    private int experience;
    private String uname;
    private Double balance;
}
