package com.lyn521.controller;

import com.lyn521.pojo.Address;
import com.lyn521.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("address")
public class AddressController {
    @Autowired
    AddressService addressService;
    @Autowired(required = false)
    HttpServletRequest request;

    @GetMapping("my")
    public Object getMyAddress() {
        int uid = request.getIntHeader("id");
        return addressService.getMyAddress(uid);
    }

    @PostMapping("getAddressById")
    public Object getAddressById(Address address) {
        return addressService.getAddressById(address.getId());
    }

    @PostMapping("add")
    public Object add(Address address) {
        int uid = request.getIntHeader("id");
        address.setUid(uid);
        return addressService.addAddress(address);
    }

    @PostMapping("del")
    public Object del(Address address) {
        return addressService.removeAddress(address);
    }

    @PostMapping("update")
    public Object update(Address address) {
        return addressService.updateAddress(address);
    }

    @GetMapping("getAddressByUId")
    public Object getAddressByUId() {
        int id = request.getIntHeader("id");
        return addressService.getAddressByUId(id);
    }

}
