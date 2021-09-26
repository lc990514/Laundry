package com.lyn521.controller;

import com.lyn521.pojo.MerchantUser;
import com.lyn521.pub.back.MsgCode;
import com.lyn521.pub.back.Result;
import com.lyn521.service.MerchantUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;

@RestController
@RequestMapping("me")
public class MerchantUserController {
    @Autowired
    private MerchantUserService userService;
    @Autowired(required = false)
    private HttpServletRequest request;

    @RequestMapping("all")
    public Object getSUserAll(int page, int limit) {
        return userService.getSUserAll(page, limit);
    }

    @RequestMapping("del")
    public Object delSUserById(@PathParam("id") String id) {
        return userService.delSUserById(id);
    }

    @RequestMapping("getUserById")
    public Object getSUserById(@PathParam("id") int id) {
        return userService.getSUerById(id);
    }

    @RequestMapping("rest")
    public Object restUser(MerchantUser user) {
        return userService.restSUser(user);
    }

    @RequestMapping("add")
    public Object addUser(MerchantUser user) {
        return userService.addSUser(user);
    }

    @PostMapping("/login")
    public Object login(@PathParam("username") String username,@PathParam("password") String password,@PathParam("identity") String identity){
        MerchantUser merchantUser = new MerchantUser();
        merchantUser.setUsername(username);
        merchantUser.setPassword(password);
        if (identity.equals("merchant")){
            return userService.loginSUser(merchantUser);
        }
        return Result.error(MsgCode.JURISDICTION_ERROR);
    }

    @PostMapping("/token")
    public Object token(){
        int id = request.getIntHeader("id");
        String token = request.getHeader("token");
        MerchantUser merchantUser = new MerchantUser();
        merchantUser.setId(id);
        merchantUser.setToken(token);
        return userService.token(merchantUser);
    }

    @PostMapping("/restMsg")
    public Object restMsg(MerchantUser user){
        return userService.restMsg(user);
    }

    @PostMapping("/getData")
    public Object getData(@PathParam("id") int id){
        return userService.getData(id);
    }

    @PostMapping("/register")
    public Object registerSUser(@PathParam("username") String username,@PathParam("password") String password,@PathParam("uname") String uname){
        System.out.println(username+"===="+password+"===="+uname);
        if (username==null || password==null || uname==null){
            return Result.error(MsgCode.NOT_GET_DATA);
        }
        MerchantUser user = new MerchantUser();
        user.setUsername(username);
        user.setPassword(password);
        user.setUname(uname);
        return userService.registerSUser(user);
    }

    @PostMapping("/restPassword")
    public Object restPassword(MerchantUser user){
        return userService.restPassword(user);
    }

    @GetMapping("/get/all")
    public Object getAll(){
        return userService.getAll();
    }

    @PostMapping("/get/ById")
    public Object getById(MerchantUser merchantUser){

        return userService.getSUerById(merchantUser.getId());
    }
}
