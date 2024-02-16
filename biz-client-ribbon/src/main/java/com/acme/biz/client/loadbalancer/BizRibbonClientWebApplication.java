package com.acme.biz.client.loadbalancer;

import com.acme.biz.api.interfaces.UserRegistrationService;
import com.acme.biz.api.model.User;
import com.acme.biz.client.loadbalancer.ribbon.UserServiceRibbonClientConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xzm
 * @description
 */

@SpringBootApplication
@EnableFeignClients(clients = UserRegistrationService.class)
@RibbonClient(name = "user-service",configuration = UserServiceRibbonClientConfiguration.class)
@RestController
@EnableDiscoveryClient
public class BizRibbonClientWebApplication {


    @Autowired
    private UserRegistrationService userRegistrationService;

    @GetMapping("/user/register")
    public Object registerUser() {
        User user = new User();
        return userRegistrationService.registerUser(user);
    }

    public static void main(String[] args) {
        SpringApplication.run(BizRibbonClientWebApplication.class, args);
    }
}