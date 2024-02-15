package com.acme.biz.web;

import com.acme.biz.web.mvc.interceptor.ResourceBulkheadHandlerInterceptor;
import com.acme.biz.web.mvc.servlet.tomcat.MyTomcatWebServerFactoryCustomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@SpringBootApplication
@EnableConfigurationProperties
@EnableDiscoveryClient // 激活服务发现客户端
@ServletComponentScan
@EnableScheduling
//@Import(ResourceBulkheadHandlerInterceptor.class)
public class MyBizApplication  implements WebMvcConfigurer {

//    @Autowired
//    private List<HandlerInterceptor> handlerInterceptors;
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        handlerInterceptors.forEach(registry::addInterceptor);
//    }

    public static void main(String[] args) {
        SpringApplication.run(MyBizApplication.class, args);
    }


}
