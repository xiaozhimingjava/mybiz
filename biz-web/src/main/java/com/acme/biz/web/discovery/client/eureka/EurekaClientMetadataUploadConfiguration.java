package com.acme.biz.web.discovery.client.eureka;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.discovery.EurekaClient;
import com.sun.management.OperatingSystemMXBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.ClassUtils;

import javax.annotation.PostConstruct;
import java.lang.management.ManagementFactory;
import java.util.Map;

/**
 * @author xzm
 * @description
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(name = "com.netflix.discovery.DiscoveryClient")
public class EurekaClientMetadataUploadConfiguration {

    @Autowired
    private EurekaClient eurekaClient;
    private ApplicationInfoManager applicationInfoManager;


    private Boolean IS_HOTSPOT = ClassUtils.isPresent("com.sun.management.OperatingSystemMXBean", null);


    @PostConstruct
    public void init() {
        this.applicationInfoManager = eurekaClient.getApplicationInfoManager();
    }


    @Scheduled(fixedRate = 5000L, initialDelay = 10L)
    public void upload() {
        Map<String, String> metadata = applicationInfoManager.getEurekaInstanceConfig().getMetadataMap();
        metadata.put("timestamp", String.valueOf(System.currentTimeMillis()));
        metadata.put("cpu-usage", String.valueOf(getCpuUsage()));
        applicationInfoManager.registerAppMetadata(metadata);
    }

    private Integer getCpuUsage() {
        if (IS_HOTSPOT) {
            OperatingSystemMXBean operatingSystemMXBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
            Double cpuLoaded = (operatingSystemMXBean.getProcessCpuLoad() * 100);
            return cpuLoaded.intValue();
        } else {
            return 0;
        }
    }


}