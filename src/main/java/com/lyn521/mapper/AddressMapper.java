package com.lyn521.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lyn521.pojo.Address;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface AddressMapper extends BaseMapper<Address> {
}
