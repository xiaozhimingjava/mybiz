package com.acme.biz.web.client.cloud.loadbalencer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.DefaultResponse;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

/**
 * @author xzm
 * @description
 */
public class CpuUsageLoadBalancer implements ReactorServiceInstanceLoadBalancer {

    private static Logger logger = LoggerFactory.getLogger(CpuUsageLoadBalancer.class);


    private ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;

    /**
     * @param serviceInstanceListSupplierProvider a provider of
     *                                            {@link ServiceInstanceListSupplier} that will be used to get available instances
     * @param serviceId                           id of the service for which to choose an instance
     */
    public CpuUsageLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider) {
        this.serviceInstanceListSupplierProvider = serviceInstanceListSupplierProvider;
    }

    public Mono<Response<ServiceInstance>> choose(Request request) {
        ServiceInstanceListSupplier serviceInstanceListSupplier = serviceInstanceListSupplierProvider.getIfAvailable();
        Flux<List<ServiceInstance>> flux = serviceInstanceListSupplier.get();
        List<ServiceInstance> serviceInstances = flux.blockFirst();
        serviceInstances.forEach(serviceInstance -> {
            Map<String, String> metadata = serviceInstance.getMetadata();
            String cpuUsages = metadata.get("cpu-usage");

        });
        return Mono.just(new DefaultResponse(serviceInstances.get(0)));
    }
//
//        /**
//     * 兼容老版本
//     *
//     * @param request
//     * @return
//     */
//    public Mono<org.springframework.cloud.client.loadbalancer.reactive.Response<ServiceInstance>> choose(
//            org.springframework.cloud.client.loadbalancer.reactive.Request request) {
//        ServiceInstanceListSupplier serviceInstanceListSupplier = serviceInstanceListSupplierProvider.getIfAvailable();
//        Flux<List<ServiceInstance>> flux = serviceInstanceListSupplier.get();
//        List<ServiceInstance> serviceInstances = flux.blockFirst();
//        return Mono.justOrEmpty(new org.springframework.cloud.client.loadbalancer.reactive.DefaultResponse(serviceInstances.get(0)));
//    }
}