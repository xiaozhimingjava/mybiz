package com.acme.biz.api.micrometer.feign;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author xzm
 * @description
 */
public abstract class Micrometers {

    private static final ExecutorService asyncExecutor = Executors.newSingleThreadExecutor();



    public static void async(Runnable runnable) {
        asyncExecutor.execute(runnable);
    }

}