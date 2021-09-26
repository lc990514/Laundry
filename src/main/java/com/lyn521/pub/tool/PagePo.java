package com.lyn521.pub.tool;


import lombok.Data;

//分页类
@Data
public class PagePo {
    //总数
    private int count;
    private int page;
    private int limit;
    private Object data;

    public PagePo(int count, Object data) {
        this.count = count;
        this.data = data;
    }
}
