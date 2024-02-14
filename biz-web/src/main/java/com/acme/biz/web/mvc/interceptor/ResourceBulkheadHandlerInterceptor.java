package com.acme.biz.web.mvc.interceptor;

import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadConfig;
import io.github.resilience4j.bulkhead.internal.SemaphoreBulkhead;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @see SemaphoreBulkhead
 */
public class ResourceBulkheadHandlerInterceptor implements HandlerInterceptor, InitializingBean, DisposableBean {

    private Map<String, Bulkhead> bulkheadCache;
    private BulkheadConfig config;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断是否需要限流

        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Bulkhead bulkhead = getBulkhead(handlerMethod);
            bulkhead.acquirePermission();
        }
        return true;
    }

    private String getResourceName(HandlerMethod handlerMethod) {
        Method method = handlerMethod.getMethod();
        AnnotationAttributes annotationAttributes = AnnotatedElementUtils.getMergedAnnotationAttributes(method, RequestMapping.class);

        String[] path = annotationAttributes.getStringArray("path");
        if (path!=null &&path.length>0){
            return path[0];

        }
        return null;
    }

    private Bulkhead getBulkhead(HandlerMethod handlerMethod) {
        String resourceName = getResourceName(handlerMethod);
        return bulkheadCache.computeIfAbsent(resourceName, n -> Bulkhead.of(resourceName, config));
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Bulkhead bulkhead = getBulkhead(handlerMethod);
            bulkhead.releasePermission();
        }
    }

    @Override
    public void destroy() throws Exception {

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.config = BulkheadConfig.custom().build();

        bulkheadCache = new ConcurrentHashMap<>();
    }
}
