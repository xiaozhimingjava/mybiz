package com.acme.biz.web.client.rest;

import com.acme.biz.api.model.User;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonInputMessage;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public class ValidatingClientHttpRequestInterceptor implements ClientHttpRequestInterceptor, Ordered {

    private final Validator validator;

    private final HttpMessageConverter[] httpMessageConverters;

    public ValidatingClientHttpRequestInterceptor(Validator validator, HttpMessageConverter... httpMessageConverters) {
        this.validator = validator;
        this.httpMessageConverters = httpMessageConverters;
    }


    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

        ClientHttpResponse clientHttpResponse = null;

        boolean valid = beforeExecute(request, body);
        HttpHeaders headers = request.getHeaders();
        headers.add("validation-result", Boolean.toString(valid));
        clientHttpResponse = execution.execute(request, body);

        clientHttpResponse = afterExecute(clientHttpResponse);
        return clientHttpResponse;
    }

    private boolean beforeExecute(HttpRequest request, byte[] body) {
        return validateBean(request, body);

    }

    private boolean validateBean(HttpRequest request, byte[] body) {
        Class<?> bodyClass = resolveBodyClass(request.getHeaders());
        if (bodyClass != null) {
            HttpInputMessage httpInputMessage = new MappingJacksonInputMessage(new ByteArrayInputStream(body), request.getHeaders());
            MediaType mediaType = resolveMediaType(httpInputMessage);
            for (HttpMessageConverter converter : httpMessageConverters) {
                if (converter.canRead(bodyClass, mediaType)) {
                    try {
                        Object bean = converter.read(bodyClass, httpInputMessage);
                        Set<ConstraintViolation<Object>> validations = validator.validate(bean);
                        //TODO
                        if (!validations.isEmpty()){
                            return false;
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        return true;


    }

    private MediaType resolveMediaType(HttpInputMessage httpInputMessage) {
        HttpHeaders headers = httpInputMessage.getHeaders();
        return headers.getContentType();
    }

    private Class<?> resolveBodyClass(HttpHeaders httpHeaders) {
        // 临时传递 HTTP Header
        List<String> classes = httpHeaders.remove("body-class");
        if (!ObjectUtils.isEmpty(classes)) {
            String bodyClassName = classes.get(0);
            if (StringUtils.hasText(bodyClassName)) {
                return ClassUtils.resolveClassName(bodyClassName, null);
            }
        }
        return User.class;
    }


    private ClientHttpResponse afterExecute(ClientHttpResponse clientHttpResponse) {
        //todo
        return clientHttpResponse;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
