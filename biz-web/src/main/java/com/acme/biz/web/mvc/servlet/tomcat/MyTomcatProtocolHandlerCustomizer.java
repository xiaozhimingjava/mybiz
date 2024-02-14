package com.acme.biz.web.mvc.servlet.tomcat;

import org.apache.coyote.AbstractProtocol;
import org.apache.coyote.ProtocolHandler;
import org.springframework.boot.web.embedded.tomcat.TomcatProtocolHandlerCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;

public class MyTomcatProtocolHandlerCustomizer implements TomcatProtocolHandlerCustomizer {

    private final TomcatServletWebServerFactory factory;


    public MyTomcatProtocolHandlerCustomizer(TomcatServletWebServerFactory factory) {
        this.factory = factory;
    }

    @Override
    public void customize(ProtocolHandler protocolHandler) {
        if (protocolHandler instanceof AbstractProtocol) {
            AbstractProtocol protocol = (AbstractProtocol) protocolHandler;
            protocol.setMaxThreads(100);
        }

    }
}
