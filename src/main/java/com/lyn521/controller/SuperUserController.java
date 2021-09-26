package com.lyn521.controller;

import com.lyn521.pojo.SuperUser;
import com.lyn521.pub.back.MsgCode;
import com.lyn521.pub.back.Result;
import com.lyn521.service.SuperUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;

@RestController
@CrossOrigin("*")
@RequestMapping("/su")
public class SuperUserController {
    @Autowired
    private SuperUserService superUserService;
    @Autowired(required = false)
    private HttpServletRequest request;

    @PostMapping("/login")
    public Object login(@PathParam("username") String username, @PathParam("password") String password, @PathParam("identity") String identity) {
        SuperUser superUser = new SuperUser();
        superUser.setUsername(username);
        superUser.setPassword(password);
        if (identity.equals("admin")){
            return superUserService.login(superUser);
        }
        return Result.error(MsgCode.JURISDICTION_ERROR);
    }

    @PostMapping("/token")
    public Object catToken() {
        int id = request.getIntHeader("id");
        String token = request.getHeader("token");
        SuperUser superUser = new SuperUser();
        superUser.setId(id);
        superUser.setToken(token);
        return superUserService.token(superUser);
    }

    @PostMapping("/getUserById")
    public Object getUserById(int id) {
        return superUserService.getUserById(id);
    }

    @PostMapping("/restUser")
    public Object restUserById(SuperUser user) {
        return superUserService.restUserById(user);
    }

    @PostMapping("/getData")
    public Object getChartData(){
        return superUserService.getChartData();
    }

    @PostMapping("/restPassword")
    public Object restPassword(SuperUser user){
        return superUserService.restPassword(user);
    }

}
