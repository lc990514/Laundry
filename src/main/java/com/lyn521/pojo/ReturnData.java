package com.lyn521.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReturnData {
    private Double money;
    private int orders;
    private int comments;
    private int setMeals;
}
