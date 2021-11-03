package com.example.demo.bean;

import com.example.demo.MyInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

//@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private MyInterceptor timeInterceptor;

    @Bean
    public WebConfig getMyWebMvcConfig(){
        WebConfig myWebMvcConfig = new WebConfig() {
            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                registry.addViewController("/").setViewName("alogin");
                registry.addViewController("/login").setViewName("alogin");
                registry.addViewController("/main.html").setViewName("dashboard");
            }
            //注册拦截器
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(new MyInterceptor()).addPathPatterns("/**")
                        .excludePathPatterns("/login","/","/user/login");
            }
        };
        return myWebMvcConfig;
    }


}
