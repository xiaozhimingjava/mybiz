package com.acme.biz.web.client.loadbalancer;

import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

public class UserServiceServiceInstanceListSupplier implements ServiceInstanceListSupplier {
    @Override
    public String getServiceId() {
        return "user-service";
    }

    @Override
    public Flux<List<ServiceInstance>> get() {
        DefaultServiceInstance instance = new DefaultServiceInstance();
        instance.setInstanceId("user-service");
        instance.setHost("127.0.0.1");
        instance.setPort(8080);
        return Flux.just(Arrays.asList(instance));
    }
}
