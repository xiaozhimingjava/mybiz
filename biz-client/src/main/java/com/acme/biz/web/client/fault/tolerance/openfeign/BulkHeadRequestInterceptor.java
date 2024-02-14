package com.acme.biz.web.client.fault.tolerance.openfeign;

import feign.Request;
import feign.RequestInterceptor;
import feign.RequestTemplate;

public class BulkHeadRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        Request request = template.request();

        RequestTemplate requestTemplate = request.requestTemplate();


    }
}
