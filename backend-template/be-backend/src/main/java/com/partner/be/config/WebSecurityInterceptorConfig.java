package com.partner.be.config;

import com.partner.be.common.filter.AuthenticationInterceptor;
import com.partner.be.common.filter.AuthorizationInterceptor;
import com.partner.be.common.filter.CheckSubmitTokenInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


/**
 * <br>Class Name: WebSecurityInterceptorConfig
 * <br>Description: Login permission verification interceptor configuration
 * <p/>
 * <br>Created Time: February 23, 2018
 * <br>Version: 1.0
 * <br>
 * <br>History: (Version) Author Time Comments
 */
@Configuration
@Slf4j
public class WebSecurityInterceptorConfig extends WebMvcConfigurerAdapter {


    @Autowired
    WebSecurityPathConfig webSecurityPathConfig;

    @Bean
    public AuthenticationInterceptor getAuthenticationInterceptor() {
        return new AuthenticationInterceptor(webSecurityPathConfig.authenticationExclude);
    }

    @Bean
    public AuthorizationInterceptor getAuthorizationInterceptor() {
        return new AuthorizationInterceptor(webSecurityPathConfig.authorizationExclude);
    }


    @Bean
    public CheckSubmitTokenInterceptor getSubmitTokenInterceptor() {
        return new CheckSubmitTokenInterceptor(webSecurityPathConfig.checkSubmitTokenExclude);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // TODO Add back
//        InterceptorRegistration authenticationInterceptor = registry.addInterceptor(getAuthenticationInterceptor());
//        authenticationInterceptor.excludePathPatterns(StringUtils.join(webSecurityPathConfig.authenticationExclude, ","));
//        InterceptorRegistration authorizationInterceptor = registry.addInterceptor(getAuthorizationInterceptor());
//        authorizationInterceptor.excludePathPatterns(StringUtils.join(webSecurityPathConfig.authorizationExclude, ","));
//        InterceptorRegistration submitTokenInterceptor = registry.addInterceptor(getSubmitTokenInterceptor());
//        submitTokenInterceptor.excludePathPatterns(StringUtils.join(webSecurityPathConfig.checkSubmitTokenExclude, ","));

        InterceptorRegistration authenticationInterceptor = registry.addInterceptor(getAuthenticationInterceptor());
        authenticationInterceptor.excludePathPatterns("/**");
    }

}

