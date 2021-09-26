package com.lyn521.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyn521.mapper.CommentsMapper;
import com.lyn521.mapper.MerchantUserMapper;
import com.lyn521.mapper.RecordsMapper;
import com.lyn521.mapper.UserMapper;
import com.lyn521.pojo.Comments;
import com.lyn521.pojo.MerchantUser;
import com.lyn521.pojo.Records;
import com.lyn521.pojo.User;
import com.lyn521.pub.back.MsgCode;
import com.lyn521.pub.back.Result;
import com.lyn521.pub.tool.PagePo;
import com.lyn521.pub.tool.SHA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Repository
public class CommentsServiceImpl implements CommentsService {
    @Autowired
    private CommentsMapper commentsMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MerchantUserMapper merchantUserMapper;
    @Autowired
    private RecordsMapper recordsMapper;

    @Override
    public Object getAllComments(int page, int limit) {
        IPage<Comments> page1 = new Page<>(page, limit);
        LambdaQueryWrapper<Comments> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(
                Comments::getId,
                Comments::getUserId,
                Comments::getSuserId,
                Comments::getMessages,
                Comments::getGrade
        );
        page1 = commentsMapper.selectPage(page1, queryWrapper);
        PagePo pagePo = new PagePo((int) page1.getTotal(), page1.getRecords());
        return Result.success(pagePo);
    }

    @Override
    public Object delCommentsById(String id) {
        String[] ids = id.split(";");
        int row = 0;
        for (int i = 0; i < ids.length; i++) {
            row += commentsMapper.deleteById(ids[i]);
        }
        if (row>0){
            return Result.success(null);
        }
        return Result.error(MsgCode.SERVER_ERROR);
    }

    @Override
    public Object getAllBySId(int page, int limit, int id) {
        IPage<Comments> page1 = new Page<>(page, limit);
        LambdaQueryWrapper<Comments> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(
                Comments::getId,
                Comments::getUserId,
                Comments::getSuserId,
                Comments::getMessages,
                Comments::getGrade
        ).eq(Comments::getSuserId,id);
        page1 = commentsMapper.selectPage(page1, queryWrapper);
        PagePo pagePo = new PagePo((int) page1.getTotal(), page1.getRecords());
        return Result.success(pagePo);
    }

    @Override
    public Object addComments(Comments comments) {
        int row = commentsMapper.insert(comments);
        if (row<=0){
            return Result.error(MsgCode.SERVER_ERROR);
        }
        //用户经验
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.select(User::getExperience, User::getGrade).eq(User::getId, comments.getUserId());
        User user = userMapper.selectOne(userLambdaQueryWrapper);
        if (user == null) {
            return Result.error(MsgCode.SERVER_ERROR);
        }
        int grade = (user.getExperience() + 1) / 100;
        if (grade > 5) {
            grade = 5;
        }
        user.setGrade(grade);
        LambdaUpdateWrapper<User> userLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        userLambdaUpdateWrapper
                .set(User::getExperience, user.getExperience() + 1)
                .set(User::getGrade, user.getGrade())
                .eq(User::getId, comments.getUserId());
        row = userMapper.update(null, userLambdaUpdateWrapper);
        if (row <= 0) {
            return Result.error(MsgCode.SERVER_ERROR);
        }
        //商户经验
        LambdaQueryWrapper<MerchantUser> merchantUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        merchantUserLambdaQueryWrapper
                .select(MerchantUser::getGrade, MerchantUser::getExperience)
                .eq(MerchantUser::getId, comments.getSuserId());
        MerchantUser merchantUser = merchantUserMapper.selectOne(merchantUserLambdaQueryWrapper);
        if (merchantUser == null) {
            return Result.error(MsgCode.SERVER_ERROR);
        }
        grade = (merchantUser.getExperience() + comments.getGrade()) / 100;
        if (grade > 5) {
            grade = 5;
        }
        merchantUser.setGrade(grade);
        LambdaUpdateWrapper<MerchantUser> merchantUserLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        merchantUserLambdaUpdateWrapper
                .set(MerchantUser::getExperience,merchantUser.getExperience()+comments.getGrade())
                .set(MerchantUser::getGrade,merchantUser.getGrade())
                .eq(MerchantUser::getId,comments.getSuserId());
        row = merchantUserMapper.update(null, merchantUserLambdaUpdateWrapper);
        if (row<=0){
            return Result.error(MsgCode.SERVER_ERROR);
        }
        //添加事件
        Records records = new Records();
        records.setMessages("用户评论");
        records.setUserId(comments.getSuserId());
        records.setTime(SHA.getNowTime());
        int insert = recordsMapper.insert(records);
        if (insert<0){
            return Result.error(MsgCode.SERVER_ERROR);
        }
        return Result.success(1);
    }

    @Override
    public Object getBySId(int id) {
        LambdaQueryWrapper<Comments> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(
                Comments::getId,
                Comments::getUserId,
                Comments::getSuserId,
                Comments::getMessages,
                Comments::getGrade
        ).eq(Comments::getSuserId,id);
        List<Comments> comments = commentsMapper.selectList(lambdaQueryWrapper);
        return Result.success(comments);
    }
}
