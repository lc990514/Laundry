package com.lyn521.service;

import com.lyn521.pojo.SuperUser;

public interface SuperUserService {
    /**
     * 登录
     *
     * @param user
     * @return
     */
    Object login(SuperUser user);

    /**
     * token验证
     *
     * @param user
     * @return
     */
    Object token(SuperUser user);

    /**
     * 根据id获取用户
     *
     * @param id
     * @return
     */
    Object getUserById(int id);

    /**
     * 根据id更新用户
     *
     * @param user
     * @return
     */
    Object restUserById(SuperUser user);

    /**
     * 获取画图所需要的数据
     * @return
     */
    Object getChartData();

    Object restPassword(SuperUser user);
}
