package com.acme.biz.data.fault.tolerance.mybatis;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Invocation;

import java.util.List;
import java.util.Properties;

public class Resilience4jMyBatisInterceptor implements Interceptor {

    private final List<ExecutorDecorator> executorDecorators;

    public Resilience4jMyBatisInterceptor(List<ExecutorDecorator> executorDecorators) {
        this.executorDecorators = executorDecorators;
    }


    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return decorateExecutor((Executor) target);
        }
        return Interceptor.super.plugin(target);
    }

    private Object decorateExecutor(Executor target) {
        return new ExecutorDecorators(target, executorDecorators);
    }

    @Override
    public void setProperties(Properties properties) {
        Interceptor.super.setProperties(properties);
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        return invocation.proceed();
    }
}
