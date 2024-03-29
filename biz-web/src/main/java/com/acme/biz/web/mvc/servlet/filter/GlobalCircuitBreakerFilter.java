/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.acme.biz.web.mvc.servlet.filter;


import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * 全局 {@link CircuitBreaker} Filter
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
@WebFilter(filterName = "globalCircuitBreakerFilter", urlPatterns = "/*",
        dispatcherTypes = {
                DispatcherType.REQUEST
        })
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalCircuitBreakerFilter implements Filter {

    private volatile CircuitBreaker circuitBreaker;


    private CircuitBreakerRegistry circuitBreakerRegistry;


    private String name;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig
                .custom()
                .build();

        circuitBreakerRegistry =
                CircuitBreakerRegistry.of(circuitBreakerConfig);
        String filterName = filterConfig.getFilterName();
        name = "CircuitBreaker-" + filterName;
        this.circuitBreaker = circuitBreakerRegistry.circuitBreaker(name);
    }


    public void setCircuitBreaker(CircuitBreaker circuitBreaker) {
        this.circuitBreaker = circuitBreaker;
    }

    public String getName() {
        return this.name;
    }

    public CircuitBreakerRegistry getCircuitBreakerRegistry() {
        return circuitBreakerRegistry;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 粗粒度的实现
        circuitBreaker.acquirePermission();
        final long start = circuitBreaker.getCurrentTimestamp();
        try {
            chain.doFilter(request, response); //d
            long duration = circuitBreaker.getCurrentTimestamp() - start;
            circuitBreaker.onSuccess(duration, circuitBreaker.getTimestampUnit());
        } catch (Throwable e) {
            // Do not handle java.lang.Error
            long duration = circuitBreaker.getCurrentTimestamp() - start;
            circuitBreaker.onError(duration, circuitBreaker.getTimestampUnit(), e);
            throw e;
        }

//        try {
//            circuitBreaker.decorateCheckedRunnable(() -> chain.doFilter(request, response)).run();
//        } catch (Throwable e) {
//            throw new ServletException(e);
//        }

    }

    @Override
    public void destroy() {
    }
}
