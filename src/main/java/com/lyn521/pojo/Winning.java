package com.lyn521.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_winning")
public class Winning {
    private int id;
    private String time;
    private String message;
    private int userId;
}
