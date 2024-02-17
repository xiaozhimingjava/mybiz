package com.acme.biz.web.client.cloud;

import com.acme.biz.api.interfaces.UserRegistrationService;
import com.acme.biz.api.micrometer.feign.FeignCallCounterMetrics;
import com.acme.biz.web.client.cloud.controller.BizClientFeignController;
import com.acme.biz.web.client.cloud.loadbalencer.CpuUsageBalancerConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * biz-client 启动类
 * @author xzm
 * @description
 */
@ComponentScan
@EnableAutoConfiguration
@EnableDiscoveryClient
@LoadBalancerClient(name = "user-service", configuration = CpuUsageBalancerConfiguration.class)
@EnableScheduling
@EnableFeignClients(clients = UserRegistrationService.class)
@Import(FeignCallCounterMetrics.class)
public class BizClientApplication {

    @Autowired
    private BizClientFeignController bizClientFeignController;

    public static void main(String[] args) {
        SpringApplication.run(BizClientApplication.class, args);
    }

    @Scheduled(fixedRate = 10 * 1000L)
    public void registerUser() {
        System.out.println(bizClientFeignController.registerUser());
    }
}