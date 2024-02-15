package com.acme.biz.web.discovery.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Map;

/**
 * @author xzm
 * @description
 */
@ConditionalOnClass(name = {
        "org.springframework.cloud.client.discovery.DiscoveryClient",
        "org.springframework.cloud.client.serviceregistry.ServiceRegistry"
})
@Configuration(proxyBeanMethods = false)
public class MetadataUploadConfiguration {

    @Autowired
    private ServiceRegistry serviceRegistry;

    @Autowired
    private Registration registration;




    @Scheduled(fixedRate = 5000L, initialDelay = 10L)
    public void upload() {
        Map<String, String> metadata = registration.getMetadata();
        metadata.put("timestamp", String.valueOf(System.currentTimeMillis()));
        serviceRegistry.deregister(registration);
        serviceRegistry.register(registration);
    }
}