package com.acme.biz.webflux.filter;

import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadConfig;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ResourceBulkheadFilter implements WebFilter , InitializingBean, DisposableBean {


    private Map<String, Bulkhead> bulkheadCache;
    private BulkheadConfig config;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String  resourceName = request.getPath().toString();
        bulkheadCache.computeIfAbsent(resourceName, n -> Bulkhead.of(resourceName, config));
        Bulkhead bulkhead = doGetBulkhead(resourceName);
        if (bulkhead != null) {
            bulkhead.acquirePermission();
        }
        return chain.filter(exchange);
    }

    private Bulkhead doGetBulkhead(String resourceName) {
        return bulkheadCache.get(resourceName);
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
