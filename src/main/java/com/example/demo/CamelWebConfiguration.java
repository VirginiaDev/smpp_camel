package com.example.demo;

import org.apache.camel.component.servlet.CamelHttpTransportServlet;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CamelWebConfiguration {
    private static final String CAMEL_URL_MAPPING = "/v1/*";
    private static final String CAMEL_SERVLET_NAME = "CamelServlet";
    @Bean
    public ServletRegistrationBean servletRegistrationBean() {

        CamelHttpTransportServlet camelHttpTransportServlet = new CamelHttpTransportServlet();
        camelHttpTransportServlet.setAsync(true);

        ServletRegistrationBean registration = new ServletRegistrationBean
            (camelHttpTransportServlet,CAMEL_URL_MAPPING);
        registration.setName(CAMEL_SERVLET_NAME);

        return registration;
    }
}
