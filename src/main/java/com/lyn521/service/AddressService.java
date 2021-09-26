package com.lyn521.service;

import com.lyn521.pojo.Address;

public interface AddressService {
    /**
     * 获取我的地址
     * @param uid
     * @return
     */
    Object getMyAddress(int uid);

    /**
     * 根据地址id查找地址
     * @param id
     * @return
     */
    Object getAddressById(int id);

    /**
     * 添加地址
     * @param address
     * @return
     */
    Object addAddress(Address address);

    /**
     * 更新地址
     * @param address
     * @return
     */
    Object updateAddress(Address address);

    /**
     * 删除地址
     * @param address
     * @return
     */
    Object removeAddress(Address address);

    /**
     * 根据用户id获取默认地址
     * @param id
     * @return
     */
    Object getAddressByUId(int id);
}
