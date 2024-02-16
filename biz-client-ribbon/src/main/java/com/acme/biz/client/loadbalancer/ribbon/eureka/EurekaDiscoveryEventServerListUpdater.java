package com.acme.biz.client.loadbalancer.ribbon.eureka;

import com.netflix.discovery.DiscoveryEvent;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.EurekaEvent;
import com.netflix.discovery.EurekaEventListener;
import com.netflix.loadbalancer.ServerListUpdater;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * @author xzm
 * @description
 */
public class EurekaDiscoveryEventServerListUpdater implements ServerListUpdater, EurekaEventListener {

    private static final Logger logger = LoggerFactory.getLogger(EurekaDiscoveryEventServerListUpdater.class);

    private final EurekaClient eurekaClient;

    private UpdateAction updateAction;

    private volatile long timestamp;



    public EurekaDiscoveryEventServerListUpdater(EurekaClient eurekaClient) {
        this.eurekaClient = eurekaClient;
        // 注册当前对象作为 EurekaEventListener
        eurekaClient.registerEventListener(this);
    }


    @Override
    public void start(UpdateAction updateAction) {
        this.updateAction = updateAction;
    }

    @Override
    public void stop()  {
    }

    @Override
    public String getLastUpdate() {
        return new Date(timestamp).toString();
    }

    @Override
    public long getDurationSinceLastUpdateMs() {
        return 0;
    }

    @Override
    public int getNumberMissedCycles() {
        return 0;
    }

    @Override
    public int getCoreThreads() {
        return 0;
    }

    @Override
    public void onEvent(EurekaEvent event) {
        if (event instanceof DiscoveryEvent) { // 当 Eureka 客户端更新时会发送事件 - DiscoveryEvent
            this.timestamp = ((DiscoveryEvent) event).getTimestamp();
            // 利用 CacheRefreshedEvent 事件来触发
            updateAction.doUpdate();
        }

    }
}