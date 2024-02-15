package com.acme.biz.api.openfeign;

import com.acme.biz.api.ApiRequest;
import com.acme.biz.api.ApiResponse;
import feign.FeignException;
import feign.Response;
import feign.codec.DecodeException;
import feign.codec.Decoder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;
import java.lang.reflect.Type;

public class ApiResponseInterceptor implements Decoder {

    private final Decoder decoder;

    private MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();


    public ApiResponseInterceptor(Decoder decoder) {
        this.decoder = decoder;
    }


    @Override
    public Object decode(Response response, Type bodyType) throws IOException, DecodeException, FeignException {
        if (!bodyType.getClass().equals(ApiResponse.class)) {
            Object object = decoder.decode(response, ApiResponse.class);
            if (object instanceof  ApiResponse){
                return ((ApiResponse<?>) object).getBody();
            }
        }
        return decoder.decode(response, bodyType);
    }
}
