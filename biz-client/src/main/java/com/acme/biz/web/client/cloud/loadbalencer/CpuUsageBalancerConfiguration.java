package com.acme.biz.web.client.cloud.loadbalencer;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @author xzm
 * @description
 */
@Configuration(proxyBeanMethods = false)
public class CpuUsageBalancerConfiguration {


    @Bean
    public ReactorLoadBalancer<ServiceInstance> cpuUsageLoadBalancer(Environment environment,
                                                                     LoadBalancerClientFactory loadBalancerClientFactory) {
        String name = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
        return new CpuUsageLoadBalancer(loadBalancerClientFactory.getLazyProvider(name, ServiceInstanceListSupplier.class));
    }
}