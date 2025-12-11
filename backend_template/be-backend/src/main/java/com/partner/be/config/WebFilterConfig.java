package com.partner.be.config;

import com.partner.be.backend.common.security.JwtAuthenticationFilter;
import com.partner.be.backend.common.security.JwtTokenProvider;
import com.partner.be.common.filter.LogUserNameFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
public class WebFilterConfig {

  //    @Bean
  //    public FilterRegistrationBean setUserInfo() {
  //        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new
  // SetLoginUserInfoFilter());
  //        filterRegistrationBean.addUrlPatterns("/*");
  //        return filterRegistrationBean;
  //    }

  @Bean
  public JwtAuthenticationFilter jwtAuthenticationFilterBean(
      WebSecurityPathConfig pathConfig,
      JwtTokenProvider tokenProvider,
      JwtProperties jwtProperties) {
    return new JwtAuthenticationFilter(pathConfig, tokenProvider, jwtProperties);
  }

  @Bean
  public FilterRegistrationBean<JwtAuthenticationFilter> jwtAuthenticationFilterRegistration(
      JwtAuthenticationFilter filter) {
    FilterRegistrationBean<JwtAuthenticationFilter> registrationBean =
        new FilterRegistrationBean<>(filter);
    registrationBean.addUrlPatterns("/*");
    registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
    return registrationBean;
  }

  @Bean
  public FilterRegistrationBean<LogUserNameFilter> logUserName() {
    FilterRegistrationBean<LogUserNameFilter> filterRegistrationBean =
        new FilterRegistrationBean<>(new LogUserNameFilter());
    filterRegistrationBean.addUrlPatterns("/*");
    filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);
    return filterRegistrationBean;
  }
}
