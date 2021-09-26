package com.lyn521.service;

import com.lyn521.pojo.MerchantUser;

public interface MerchantUserService {
    /**
     * 获取全部商户
     * @param page
     * @param limit
     * @return
     */
    Object getSUserAll(int page, int limit);

    /**
     * 删除商户根据id
     * @param id
     * @return
     */
    Object delSUserById(String id);

    /**
     * 根据id获取商户
     * @param id
     * @return
     */
    Object getSUerById(int id);

    /**
     * 更新商户
     * @param user
     * @return
     */
    Object restSUser(MerchantUser user);

    /**
     * 添加商户
     * @param user
     * @return
     */
    Object addSUser(MerchantUser user);

    /**
     * 登录
     * @param user
     * @return
     */
    Object loginSUser(MerchantUser user);

    /**
     * 注册
     * @param user
     * @return
     */
    Object registerSUser(MerchantUser user);

    /**
     * token 验证自动登录
     * @param user
     * @return
     */
    Object token(MerchantUser user);

    /**
     * 修改基本信息
     * @param user
     * @return
     */
    Object restMsg(MerchantUser user);

    /**
     * 根据用户id获取业绩数据
     * @param id
     * @return
     */
    Object getData(int id);

    Object restPassword(MerchantUser user);

    /**
     * 不分页查询
     * @return
     */
    Object getAll();
}
