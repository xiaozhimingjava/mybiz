package com.acme.biz.web.mvc.servlet.tomcat;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.core.Ordered;


/**
 * 自定义 {@link  WebServerFactoryCustomizer}
 */
public class MyTomcatWebServerFactoryCustomizer implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {
    @Override
    public void customize(TomcatServletWebServerFactory factory) {
        factory.addProtocolHandlerCustomizers(new MyTomcatProtocolHandlerCustomizer(factory));
    }


}
