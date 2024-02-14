package com.acme.biz.web.mvc.servlet.filter;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.autoconfigure.CircuitBreakerProperties;
import io.github.resilience4j.common.circuitbreaker.configuration.CircuitBreakerConfigurationProperties;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.ConfigurableEnvironment;

import javax.annotation.PostConstruct;

/**
 * @author xzm
 * @description
 */
@Configuration
public class DynamicCircuitBreakerConfiguration implements ApplicationContextAware {

    @Autowired
    private ConfigurableEnvironment environment;

    private Binder binder;


    private volatile CircuitBreakerProperties originCircuitBreakerProperties;

    @Autowired
    private CircuitBreakerProperties circuitBreakerProperties;

    
    private ApplicationContext applicationContext;


    @PostConstruct
    public void init() {
        Iterable<ConfigurationPropertySource> configurationPropertySources = ConfigurationPropertySources.get(environment);
        binder = new Binder(configurationPropertySources);
        bindOriginCircuitBreakerProperties();

    }

    private void bindOriginCircuitBreakerProperties() {
        BindResult<CircuitBreakerProperties> bindResult = binder.bind("resilience4j.circuitbreaker", CircuitBreakerProperties.class);
        CircuitBreakerProperties circuitBreakerProperties = bindResult.get();
        originCircuitBreakerProperties = circuitBreakerProperties;
    }


    @EventListener(EnvironmentChangeEvent.class)
    public void onEnvironmentChangeEvent(EnvironmentChangeEvent event) {
        FilterRegistrationBean filterRegistrationBean = (FilterRegistrationBean) applicationContext.getBean("globalCircuitBreakerFilter");
        GlobalCircuitBreakerFilter globalCircuitBreakerFilter = (GlobalCircuitBreakerFilter) filterRegistrationBean.getFilter();
//        GlobalCircuitBreakerFilter globalCircuitBreakerFilter = (GlobalCircuitBreakerFilter) applicationContext.getBean("globalCircuitBreakerFilter");
        String name = globalCircuitBreakerFilter.getName();
        CircuitBreakerConfigurationProperties.InstanceProperties config = circuitBreakerProperties.getConfigs().get(name);

        CircuitBreakerConfigurationProperties.InstanceProperties originConfig = originCircuitBreakerProperties.getConfigs().get(name);
        if (!config.equals(originConfig)) {
            CircuitBreakerRegistry circuitBreakerRegistry = globalCircuitBreakerFilter.getCircuitBreakerRegistry();
            CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig
                    .custom()
                    .failureRateThreshold(config.getFailureRateThreshold())
                    .build();
            CircuitBreaker circuitBreaker = CircuitBreaker.of(name,circuitBreakerConfig);
            circuitBreakerRegistry.replace(name,circuitBreaker);
            globalCircuitBreakerFilter.setCircuitBreaker(circuitBreaker);
            bindOriginCircuitBreakerProperties();
        }
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}