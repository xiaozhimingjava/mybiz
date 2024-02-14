package com.acme.biz.web.mvc.method.annotation;

import com.acme.biz.api.ApiResponse;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;

import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletResponse;


/**
 *
 * @see  org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor
 */
public class ApiResponseHandlerMethodReturnValueHandler implements HandlerMethodReturnValueHandler {

    private MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();



    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return (AnnotatedElementUtils.hasAnnotation(returnType.getContainingClass(), ResponseBody.class) ||
                returnType.hasMethodAnnotation(ResponseBody.class))
                && !ApiResponse.class.equals(returnType.getParameterType());
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
        mavContainer.setRequestHandled(true);
        ServletServerHttpResponse outputMessage = createOutputMessage(webRequest);
        //return value pojo
        ApiResponse apiResponse = ApiResponse.ok(returnValue);
        converter.write(apiResponse, MediaType.APPLICATION_JSON,outputMessage);

    }



    protected ServletServerHttpResponse createOutputMessage(NativeWebRequest webRequest) {
        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
        Assert.state(response != null, "No HttpServletResponse");
        return new ServletServerHttpResponse(response);
    }
}
