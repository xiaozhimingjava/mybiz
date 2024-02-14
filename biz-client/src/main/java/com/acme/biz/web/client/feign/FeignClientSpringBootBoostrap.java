package com.acme.biz.web.client.feign;


import com.acme.biz.api.interfaces.UserRegistrationService;
import com.acme.biz.api.model.User;
import com.acme.biz.web.client.loadbalancer.UserServiceLoadBalancerConfiguration;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@EnableAutoConfiguration
@EnableFeignClients(clients = UserRegistrationService.class)
@LoadBalancerClient(value = "user-service", configuration = UserServiceLoadBalancerConfiguration.class)
@Import(FeignConfiguration.class)
public class FeignClientSpringBootBoostrap {


    public static void main(String[] args) {
        ConfigurableApplicationContext context = new SpringApplicationBuilder(FeignClientSpringBootBoostrap.class)
                .web(WebApplicationType.NONE)
                .build()
                .run(args);

        UserRegistrationService userRegistrationService = context.getBean(UserRegistrationService.class);
        User user = new User();
        user.setId(1L);
        System.out.println(userRegistrationService.registerUser(user));
        context.close();

    }
}
