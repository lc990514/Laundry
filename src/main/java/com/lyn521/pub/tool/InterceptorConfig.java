package com.lyn521.pub.tool;

import com.lyn521.mapper.MerchantUserMapper;
import com.lyn521.mapper.SuperUserMapper;
import com.lyn521.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class InterceptorConfig extends WebMvcConfigurerAdapter {
    @Autowired()
    SuperUserMapper superUserMapper;
    @Autowired
    MerchantUserMapper merchantUserMapper;
    @Autowired
    UserMapper userMapper;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TokenInterceptor(superUserMapper,merchantUserMapper,userMapper))
                .addPathPatterns("/**")
                .excludePathPatterns("/su/login")
                .excludePathPatterns("/file/uploadImage")
                .excludePathPatterns("/file/image/*")
                .excludePathPatterns("/me/login")
                .excludePathPatterns("/me/token")
                .excludePathPatterns("/me/register")
                .excludePathPatterns("/me/get/all")
                .excludePathPatterns("/me/get/ById")
                .excludePathPatterns("/image/*")
                .excludePathPatterns("/setMeal/get/all")
                .excludePathPatterns("/setMeal/getByType")
                .excludePathPatterns("/setMeal/get/BySId")
                .excludePathPatterns("/comments/get/BySId")
                .excludePathPatterns("/user/token")
                .excludePathPatterns("/user/login")
                .excludePathPatterns("/user/register")
                .excludePathPatterns("/su/token");
    }
}
