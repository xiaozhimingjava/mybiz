package com.acme.biz.web.mvc.interceptor;

import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadConfig;
import io.github.resilience4j.bulkhead.internal.SemaphoreBulkhead;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @see SemaphoreBulkhead
 */
public class GlobalBulkheadHandlerInterceptor implements HandlerInterceptor , InitializingBean, DisposableBean {


    private Bulkhead bulkhead;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断是否需要限流

        bulkhead.acquirePermission();
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //清除状态
        bulkhead.releasePermission();
    }

    @Override
    public void destroy() throws Exception {

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        BulkheadConfig config = BulkheadConfig.custom().build();
        bulkhead = Bulkhead.of("globalBulkheadHandlerInterceptor", config);
    }
}
