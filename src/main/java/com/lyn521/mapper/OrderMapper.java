package com.lyn521.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lyn521.pojo.Order;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {
}
