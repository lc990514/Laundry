package com.lyn521.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.lyn521.mapper.AddressMapper;
import com.lyn521.pojo.Address;
import com.lyn521.pub.back.MsgCode;
import com.lyn521.pub.back.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService{
    @Autowired
    AddressMapper addressMapper;

    @Override
    public Object getMyAddress(int uid) {
        LambdaQueryWrapper<Address> addressLambdaQueryWrapper=new LambdaQueryWrapper<>();
        addressLambdaQueryWrapper
                .eq(Address::getUid,uid);
        List<Address> list=addressMapper.selectList(addressLambdaQueryWrapper);

        return Result.success(list);
    }

    @Override
    public Object getAddressById(int id) {
        LambdaQueryWrapper<Address> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Address::getId,id);
        Address address = addressMapper.selectOne(lambdaQueryWrapper);
        if (address==null){
            return Result.error(MsgCode.SERVER_ERROR);
        }
        return Result.success(address);
    }

    @Override
    public Object addAddress(Address address) {
        //新加地址是默认地址时
        if(address.isChief()){
            LambdaUpdateWrapper<Address> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.set(Address::isChief,false).eq(Address::getUid,address.getUid());
            addressMapper.update(null,lambdaUpdateWrapper);
            int row = addressMapper.insert(address);
            if (row<=0){
                return Result.error(MsgCode.SERVER_ERROR);
            }
            return Result.success(1);
        }
        //新加地址不是默认地址
        //先查询地址
        LambdaQueryWrapper<Address> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Address::getUid,address.getUid());
        List<Address> addresses = addressMapper.selectList(lambdaQueryWrapper);
        //该用户这个地址为第一个时
        if (addresses==null||addresses.size()==0){
            address.setChief(true);
            int row = addressMapper.insert(address);
            if (row<=0){
                return Result.error(MsgCode.SERVER_ERROR);
            }
            return Result.success(1);
        }
        //不是第一个地址
        int row = addressMapper.insert(address);
        if (row<=0){
            return Result.error(MsgCode.SERVER_ERROR);
        }
        return Result.success(1);
    }

    @Override
    public Object updateAddress(Address address) {
        //新地址是否为默认
        //是默认
        if (address.isChief()){
            LambdaUpdateWrapper<Address> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.set(Address::isChief,false).eq(Address::getUid,address.getUid());
            addressMapper.update(null,lambdaUpdateWrapper);
            int row = addressMapper.updateById(address);
            if (row<=0){
                return Result.error(MsgCode.SERVER_ERROR);
            }
            return Result.success(1);
        }
        //不是默认地址
        LambdaQueryWrapper<Address> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Address::getUid,address.getUid());
        List<Address> addresses = addressMapper.selectList(lambdaQueryWrapper);
        //是唯一的地址
        if (addresses.size()==1){
            address.setChief(true);
            int row = addressMapper.updateById(address);
            if (row<=0){
                return Result.error(MsgCode.SERVER_ERROR);
            }
            return Result.success(1);
        }
        //不是唯一的地址
        int row = addressMapper.updateById(address);
        if (row<=0){
            return Result.error(MsgCode.SERVER_ERROR);
        }
        //设置新默认地址
        addresses = addressMapper.selectList(lambdaQueryWrapper);
        addresses.get(0).setChief(true);
        int i = addressMapper.updateById(addresses.get(0));
        if (i<=0){
            return Result.error(MsgCode.SERVER_ERROR);
        }
        return Result.success(1);
    }

    @Override
    public Object removeAddress(Address address) {
        //查询被删除的地址
        LambdaQueryWrapper<Address> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Address::getId,address.getId());
        Address address1 = addressMapper.selectOne(lambdaQueryWrapper);
        //地址为默认地址
        if (address1.isChief()){
            //查询是否唯一
            LambdaQueryWrapper<Address> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
            lambdaQueryWrapper1.eq(Address::getUid,address1.getUid());
            List<Address> addresses = addressMapper.selectList(lambdaQueryWrapper1);
            //是唯一
            if (addresses.size()==1){
                int row = addressMapper.deleteById(address.getId());
                if (row<=0){
                    return Result.error(MsgCode.SERVER_ERROR);
                }
                return Result.success(1);
            }
            //不是唯一
            int row = addressMapper.deleteById(address.getId());
            if (row<=0){
                return Result.error(MsgCode.SERVER_ERROR);
            }
            //设置新默认地址
            addresses = addressMapper.selectList(lambdaQueryWrapper1);
            addresses.get(0).setChief(true);
            int i = addressMapper.updateById(addresses.get(0));
            if (i<=0){
                return Result.error(MsgCode.SERVER_ERROR);
            }
            return Result.success(1);
        }
        //不是默认地址
        int row = addressMapper.deleteById(address.getId());
        if (row<=0){
            return Result.error(MsgCode.SERVER_ERROR);
        }
        return Result.success(1);
    }

    @Override
    public Object getAddressByUId(int id) {
        LambdaQueryWrapper<Address> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.
                eq(Address::getUid,id)
                .eq(Address::isChief,true);
        Address address = addressMapper.selectOne(lambdaQueryWrapper);
        return Result.success(address);
    }
}
