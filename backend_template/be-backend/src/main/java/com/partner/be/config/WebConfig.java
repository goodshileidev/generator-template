package com.partner.be.config;

import com.partner.be.adviser.ExceptionDispatcherAdvice;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({MessageResourcesConfig.class, ExceptionDispatcherAdvice.class})
public class WebConfig {

}
