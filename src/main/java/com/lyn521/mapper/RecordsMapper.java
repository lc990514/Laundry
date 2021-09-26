package com.lyn521.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lyn521.pojo.Records;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RecordsMapper extends BaseMapper<Records> {
}
