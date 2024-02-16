package com.acme.biz.client.loadbalancer.ribbon;

import com.acme.biz.client.loadbalancer.ribbon.eureka.EurekaDiscoveryEventServerListUpdater;
import com.netflix.discovery.DiscoveryClient;
import com.netflix.discovery.EurekaClient;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.ServerListUpdater;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xzm
 * @description
 */
@Configuration
public class UserServiceRibbonClientConfiguration {



    @Bean
    @ConditionalOnClass(EurekaClient.class)
    @ConditionalOnMissingBean
    public ServerListUpdater eurekaDiscoveryEventServerListUpdater(EurekaClient eurekaClient) {
        return new EurekaDiscoveryEventServerListUpdater(eurekaClient);
    }


    @Bean
    @ConditionalOnMissingBean
    public IRule ribbonRule() {
        WeightOnUptimeIRule rule = new WeightOnUptimeIRule();
        return rule;
    }
}