package com.acme.biz.data.fault.tolerance.mybatis;

import org.apache.ibatis.mapping.MappedStatement;

/**
 * @author xzm
 * @description
 */
public class CircuitBreakerExecutorDecorator extends ExecutorDecorator {


    @Override
    protected void before(MappedStatement ms) {
        String resourceName = getResourceName(ms);
    }

    @Override
    protected void after(MappedStatement ms) {
        String resourceName = getResourceName(ms);
    }

    private String getResourceName(MappedStatement ms) {
        return ms.getId();
    }
}