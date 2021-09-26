package com.lyn521.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_super_admin")
public class SuperUser {
    private int id;
    private String username;
    private String password;
    private String token;
    private String time;
    private String uname;
    private String portrait;
    private String introduce;
}
