package com.acme.biz.web.mvc.servlet.tomcat;

import org.apache.coyote.AbstractProtocol;
import org.apache.coyote.ProtocolHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.boot.web.embedded.tomcat.TomcatProtocolHandlerCustomizer;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.cloud.context.environment.EnvironmentManager;
import org.springframework.cloud.context.properties.ConfigurationPropertiesRebinder;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.ConfigurableEnvironment;

import javax.annotation.PostConstruct;
import java.util.Set;

/**
 * @author xzm
 * @description
 * @see ConfigurationPropertiesRebinder
 * @see EnvironmentManager
 * @see org.springframework.boot.context.properties.ConfigurationPropertiesBinder
 */
@Configuration
public class DynamicTomcatConfiguration implements TomcatProtocolHandlerCustomizer {

    @Autowired
    private ServerProperties serverProperties;

    private volatile ServerProperties originalServerProperties;

    private AbstractProtocol protocol;

    private Binder binder;


    @Autowired
    private ConfigurableEnvironment environment;



    @PostConstruct
    public void init() {
        originalServerProperties = new ServerProperties();
        BeanUtils.copyProperties(serverProperties, originalServerProperties);
    }

    @PostConstruct
    public void init2() {
        Iterable<ConfigurationPropertySource> configurationPropertySources = ConfigurationPropertySources.get(environment);
        binder = new Binder(configurationPropertySources);
        bindOriginalServerProperties();
    }

    private void bindOriginalServerProperties() {
        BindResult<ServerProperties> bindResult = binder.bind("server", ServerProperties.class);
        ServerProperties serverProperties = bindResult.get();
        this.originalServerProperties = serverProperties;
    }

    private void buildOriginalServerProperties() {
        ServerProperties originalServerProperties = new ServerProperties();
        BeanUtils.copyProperties(serverProperties, originalServerProperties);
        this.originalServerProperties = originalServerProperties;
    }


    @EventListener(EnvironmentChangeEvent.class)
    public void onEnvironmentChangeEvent(EnvironmentChangeEvent event) {
        Set<String> keys = event.getKeys();

        if (keys.contains("server.tomcat.threads.max")) {
            setMaxThreads();
        } else if (keys.contains("server.tomcat.threads.minSpare")) { // 不足：无法匹配 server.tomcat.threads.min-spare
            setMinSpareThreads();
        }
    }

    @EventListener(EnvironmentChangeEvent.class)
    public void onEnvironmentChangeEvent2(EnvironmentChangeEvent event) {
        ServerProperties.Tomcat.Threads originThreads = originalServerProperties.getTomcat().getThreads();
        ServerProperties.Tomcat.Threads threads = serverProperties.getTomcat().getThreads();
        if (!originThreads.equals(threads)) {
            setMaxThreads();
        }
    }


//    @EventListener(EnvironmentChangeEvent.class)
    public void updateOriginalServerProperties(EnvironmentChangeEvent event) {
        buildOriginalServerProperties();
    }

    private void setMinSpareThreads() {
        int minSpaerThreads = serverProperties.getTomcat().getThreads().getMinSpare();
        protocol.setMinSpareThreads(minSpaerThreads);
    }

    private void setMaxThreads() {
        int maxThreads = serverProperties.getTomcat().getThreads().getMax();
        protocol.setMaxThreads(maxThreads);
        bindOriginalServerProperties();
    }

    @Override
    public void customize(ProtocolHandler protocolHandler) {
        if (protocolHandler instanceof AbstractProtocol) {
            this.protocol = (AbstractProtocol) protocolHandler;
        }

    }
}