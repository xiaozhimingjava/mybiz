package com.acme.biz.client.loadbalancer.ribbon;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.DiscoveryClient;
import com.netflix.discovery.EurekaClient;
import com.netflix.loadbalancer.*;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.ribbon.DefaultServerIntrospector;
import org.springframework.cloud.netflix.ribbon.ServerIntrospector;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;

import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * @author xzm
 * @description
 */
public class WeightOnUptimeIRule implements IRule {

    private ILoadBalancer lb;




    private static final Logger logger = LoggerFactory.getLogger(WeightOnUptimeIRule.class);

    private Random random = new SecureRandom();

    protected AtomicBoolean serverWeightAssignmentInProgress = new AtomicBoolean(false);



    public void init() {

    }


    @Override
    public Server choose(Object key) {
        final List<Server> accessibleServer = getLoadBalancer().getReachableServers();
        Map<Server, Integer> weightedServers = new HashMap<>();
        for (Server server : accessibleServer) {
            if (server instanceof DiscoveryEnabledServer) {
                DiscoveryEnabledServer discoveryEnabledServer = (DiscoveryEnabledServer) server;
                long serviceUpTimeStamp = discoveryEnabledServer.getInstanceInfo().getLeaseInfo().getServiceUpTimestamp();
                long uptime = System.currentTimeMillis() - serviceUpTimeStamp;
                if (uptime > 30 * 60 * 1000) {
                    weightedServers.put(server, 1);
                } else {
                    weightedServers.put(server, 0);
                }
            }
        }
        int randomValue = random.nextInt(100);
        int rangevalue = randomValue > 30 ? 1 : 0;
        List<Server> serversLongTime = weightedServers.keySet().stream().filter(server ->
                weightedServers.get(server).equals(1)
        ).collect(Collectors.toList());
        List<Server> serversShotTime = weightedServers.keySet().stream().filter(server ->
                weightedServers.get(server).equals(0)
        ).collect(Collectors.toList());
        if (rangevalue ==1 &&serversLongTime!=null && serversLongTime.size()>0){
            return serversLongTime.get(random.nextInt(serversLongTime.size()));
        }else if (serversShotTime!=null && serversShotTime.size()>0){

            return serversShotTime.get(random.nextInt(serversShotTime.size()));
        }else {
            return accessibleServer.get(0);
        }
    }

    @Override
    public void setLoadBalancer(ILoadBalancer lb) {
        this.lb = lb;
    }

    @Override
    public ILoadBalancer getLoadBalancer() {
        return lb;
    }


}

