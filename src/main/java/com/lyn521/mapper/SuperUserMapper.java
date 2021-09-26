package com.lyn521.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lyn521.pojo.SuperUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper //持久层
public interface SuperUserMapper extends BaseMapper<SuperUser> {
}
