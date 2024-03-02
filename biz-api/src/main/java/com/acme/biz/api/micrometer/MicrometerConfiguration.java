package com.acme.biz.api.micrometer;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.cloud.client.serviceregistry.Registration;

import javax.management.MBeanServer;
import java.lang.management.ManagementFactory;

import static java.util.Arrays.asList;

/**
 * @author xzm
 * @description
 */
public class MicrometerConfiguration implements MeterRegistryCustomizer {


    private static final  String SERVO_MBEAN_OBJECT_NAME_PATTERN = "com.netflix.servo:*";


    private static final MBeanServer mBeanServer =  ManagementFactory.getPlatformMBeanServer();


    @Value("${spring.application.name:default}")
    private String applicationName;

    @Autowired
    private Registration registration;


    @Override
    public void customize(MeterRegistry registry) {
        registry.config().commonTags(asList(
                Tag.of("application", applicationName),
                Tag.of("host", registration.getHost()))); // 应用维度的 Tag

    }
}