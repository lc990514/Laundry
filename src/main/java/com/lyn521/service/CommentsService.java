package com.lyn521.service;

import com.lyn521.pojo.Comments;

public interface CommentsService {
    /**
     * 查询所有评论
     *
     * @param page
     * @param limit
     * @return
     */
    Object getAllComments(int page, int limit);

    /**
     * 根据id删除评论
     *
     * @param id
     * @return
     */
    Object delCommentsById(String id);

    /**
     * 根据商户id查询评论 分页
     * @param page
     * @param limit
     * @param id
     * @return
     */
    Object getAllBySId(int page, int limit, int id);

    /**
     * 添加评论
     * @param comments
     * @return
     */
    Object addComments(Comments comments);

    Object getBySId(int id);
}
