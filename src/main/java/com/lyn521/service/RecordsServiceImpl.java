package com.lyn521.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lyn521.mapper.RecordsMapper;
import com.lyn521.pojo.Records;
import com.lyn521.pub.back.Result;
import com.lyn521.pub.tool.SHA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Repository
public class RecordsServiceImpl implements RecordsService {
    @Autowired
    private RecordsMapper recordsMapper;

    @Override
    public Object getRecordsById(int id) {
        LambdaQueryWrapper<Records> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .select(Records::getTime,Records::getMessages)
                .orderByDesc(Records::getId)
                .eq(Records::getUserId,id)
                .last("limit 15");
        List<Records> records = recordsMapper.selectList(queryWrapper);
        if (records == null || records.size()==0){
            Records records1 = new Records();
            records1.setTime(SHA.getNowTime());
            records1.setMessages("暂时还没有记录");
            records.add(records1);
        }else if (records.size()>15){
            records.subList(0,15);
        }
        return Result.success(records);
    }
}
