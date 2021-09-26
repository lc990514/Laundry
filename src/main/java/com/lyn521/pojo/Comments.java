package com.lyn521.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_comments")
public class Comments {
    private int id;
    private int suserId;
    private int userId;
    private String messages;
    private int grade;
}
