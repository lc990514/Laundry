package com.lyn521.controller;

import com.lyn521.pojo.User;
import com.lyn521.pub.back.MsgCode;
import com.lyn521.pub.back.Result;
import com.lyn521.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired(required = false)
    private HttpServletRequest request;

    @RequestMapping("all")
    public Object getSUserAll(int page, int limit) {
        return userService.getYUserAll(page, limit);
    }

    @RequestMapping("del")
    public Object delSUserById(@PathParam("id") String id) {
        return userService.delYUserById(id);
    }

    @RequestMapping("getUserById")
    public Object getSUserById(Integer id) {
        if (id == null) {
            return Result.error(MsgCode.DATA_NULL);
        }
        return userService.getYUerById(id);
    }

    @RequestMapping("rest")
    public Object restUser(User user) {
        return userService.restYUser(user);
    }

    @RequestMapping("add")
    public Object addUser(User user) {
        return userService.addYUser(user);
    }

    @GetMapping("/token")
    public Object token(User user) {
        return userService.token(user);
    }

    @PostMapping("/login")
    public Object login(User user) {
        System.out.println(user);
        return userService.login(user);
    }

    @PostMapping("/register")
    public Object register(User user) {
        return userService.register(user);
    }

    @PostMapping("/update")
    public Object update(User user) {
        user.setId(request.getIntHeader("id"));
        return userService.restYUser(user);
    }
}
