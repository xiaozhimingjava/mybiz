package com.acme.biz.web.client.feign;

import com.acme.biz.api.ApiRequest;
import com.acme.biz.api.ApiResponse;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.codec.EncodeException;
import feign.codec.Encoder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.lang.reflect.Type;

public class ApiRequestRequestInterceptor implements Encoder {

    private final  SpringEncoder springEncoder;

    private MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();


    public ApiRequestRequestInterceptor() {
        springEncoder = new SpringEncoder(new ObjectFactory<HttpMessageConverters>() {
            @Override
            public HttpMessageConverters getObject() throws BeansException {
                return new HttpMessageConverters(converter);
            }
        });
    }

    @Override
    public void encode(Object object, Type bodyType, RequestTemplate template) throws EncodeException {


        if (bodyType.getClass().equals(ApiRequest.class)&& template.headers().get("Accept").contains("application/json;v=3")) {
            ApiRequest request = new ApiRequest<>();
            request.setBody(object);
            springEncoder.encode(request,ApiRequest.class,template);
        }else {
            springEncoder.encode(object,bodyType,template);
        }

    }
}
