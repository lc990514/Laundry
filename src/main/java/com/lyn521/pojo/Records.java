package com.lyn521.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_records")
public class Records {
    private int id;
    private int userId;
    private String time;
    private String messages;
}
