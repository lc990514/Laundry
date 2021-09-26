package com.lyn521.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lyn521.pojo.Cart;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface CartMapper extends BaseMapper<Cart> {
}
