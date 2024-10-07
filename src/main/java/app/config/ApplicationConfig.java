package app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;


@Configuration
public class ApplicationConfig {

    @Autowired
    @Qualifier("handlerExceptionResolver")
    public HandlerExceptionResolver resolver;


    @Bean
    public FilterRegistrationBean someFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new CustomFilter(resolver));
        registration.addUrlPatterns("/api/*");
        registration.setName("customFilter");
        registration.setOrder(1);
        return registration;
    }



}
