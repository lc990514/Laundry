package com.lyn521.service;

import com.lyn521.pojo.User;

public interface UserService {
    /**
     * 获取全部用户
     * @param page
     * @param limit
     * @return
     */
    Object getYUserAll(int page, int limit);

    /**
     * 删除用户根据id
     * @param id
     * @return
     */
    Object delYUserById(String id);

    /**
     * 根据id查找
     * @param id
     * @return
     */
    Object getYUerById(int id);

    /**
     * 更新用户
     * @param user
     * @return
     */
    Object restYUser(User user);

    /**
     * 添加用户
     * @param user
     * @return
     */
    Object addYUser(User user);

    /**
     * token验证
     * @param user
     * @return
     */
    Object token(User user);

    /**
     * 登录
     * @param user
     * @return
     */
    Object login(User user);

    /**
     * 注册用户
     * @param user
     * @return
     */
    Object register(User user);
}
