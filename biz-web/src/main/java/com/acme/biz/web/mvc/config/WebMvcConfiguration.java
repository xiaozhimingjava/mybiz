package com.acme.biz.web.mvc.config;

import com.acme.biz.web.mvc.interceptor.ResourceBulkheadHandlerInterceptor;
import com.acme.biz.web.mvc.method.annotation.ApiResponseHandlerMethodReturnValueHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * web mvc 配置类
 */
@Configuration
public class WebMvcConfiguration {




    @Autowired
    public void resetRequestMappingHandlerAdapter(RequestMappingHandlerAdapter requestMappingHandlerAdapter){
        List<HandlerMethodReturnValueHandler> oldReturnValueHandlers = requestMappingHandlerAdapter.getReturnValueHandlers();
        List<HandlerMethodReturnValueHandler> newReturnValueHandlers = new ArrayList<>(oldReturnValueHandlers);
        newReturnValueHandlers.add(0,new ApiResponseHandlerMethodReturnValueHandler());
        requestMappingHandlerAdapter.setReturnValueHandlers(newReturnValueHandlers);
    }


}
