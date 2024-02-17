package com.acme.biz.api.micrometer.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.cloud.client.serviceregistry.Registration;

import static com.acme.biz.api.micrometer.feign.Micrometers.async;

/**
 * @author xzm
 * @description
 */
public class FeignCallCounterMetrics implements RequestInterceptor, MeterRegistryCustomizer {

    private MeterRegistry meterRegistry;

    private Counter totalCounter;

    @Autowired
    private Registration registration;


    @Value("${spring.application.name:default}")
    private String applicationName;

    @Override
    public void apply(RequestTemplate template) {
        async(()->{
            totalCounter.increment();
            Counter counter = Counter.builder("feign.calls")
                    .tag("application", applicationName)
                    .tag("instance", registration.getHost())
                    .tag("method", template.methodMetadata().configKey())
                    .register(meterRegistry);
            counter.increment();
            totalCounter.increment();
        });
    }

    @Override
    public void customize(MeterRegistry registry) {
        this.meterRegistry = registry;
        this.totalCounter = Counter.builder("feign.total-calls")
                .register(registry);
    }
}