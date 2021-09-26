package com.lyn521;

import com.lyn521.pojo.SuperUser;
import com.lyn521.pub.tool.SHA;
import com.lyn521.service.SuperUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Controller;

import java.io.UnsupportedEncodingException;


@SpringBootTest
class LaundryApplicationTests {
    @Autowired
    SuperUserService userService;

    @Test
    void getPasswordMD5(){
        System.out.println(SHA.md5("test"));
    }
    @Test
    void contextLoads() {
        SuperUser user = new SuperUser();
        user.setUsername("admin");
        user.setPassword("amin");
        System.out.println(userService.login(user));
    }
    @Test
    void password(){
        System.out.println(SHA.md5("admin"));
    }
    @Test
    void path(){
        String rootPath = this.getClass().getResource("/").getPath();
        String[] splits = rootPath.split("/");
        int breakI = 0;
        //判断项目是否被打成jar包
        boolean isJarService = false;
        for (int i = 0; i < splits.length; i++) {
            if (splits[i].contains(".jar")) {
                breakI = i;
                isJarService = true;
                break;
            }
        }
        String finalRootPath = "";
        if (isJarService) {
            for (int i = 1; i < breakI; i++) {
                finalRootPath += splits[i] + "/";
            }

        } else {
            finalRootPath = rootPath;
            finalRootPath = finalRootPath.substring(1);
        }
        try {
            finalRootPath = java.net.URLDecoder.decode(finalRootPath, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println(finalRootPath);
    }

}
