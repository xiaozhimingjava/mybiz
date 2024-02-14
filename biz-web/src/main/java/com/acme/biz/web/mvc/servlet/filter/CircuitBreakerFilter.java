package com.acme.biz.web.mvc.servlet.filter;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.vavr.CheckedRunnable;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * {@link CircuitBreaker}
 */
@WebFilter(filterName = "circuitBreakerFilter", urlPatterns = "/",dispatcherTypes = DispatcherType.REQUEST)
public class CircuitBreakerFilter implements Filter {



    private  CircuitBreakerRegistry circuitBreakerRegistry;

    private Map<String,CircuitBreaker> circuitBreakerCache;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom().build();

         circuitBreakerRegistry = CircuitBreakerRegistry.of(circuitBreakerConfig);
        String filterName = filterConfig.getFilterName();

        circuitBreakerCache = new ConcurrentHashMap<>();
    }

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String requestURI = httpServletRequest.getRequestURI();
        CircuitBreaker circuitBreaker = circuitBreakerCache.computeIfAbsent(requestURI, circuitBreakerRegistry::circuitBreaker);


        circuitBreaker.acquirePermission();
        final long start = circuitBreaker.getCurrentTimestamp();
        try {
//            consumer.accept(t);
            chain.doFilter(request,response);
            long duration = circuitBreaker.getCurrentTimestamp() - start;
            circuitBreaker.onSuccess(duration, circuitBreaker.getTimestampUnit());
        } catch (Exception exception) {
            // Do not handle java.lang.Error
            long duration = circuitBreaker.getCurrentTimestamp() - start;
            circuitBreaker.onError(duration, circuitBreaker.getTimestampUnit(), exception);
            throw exception;
        }


//        try {
//            circuitBreaker.decorateCheckedRunnable( ()-> chain.doFilter(request,response)).run();
//        } catch (Throwable e) {
//            throw new ServletException(e);
//        }


    }
}
