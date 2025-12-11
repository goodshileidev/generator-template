package com.partner.be.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;


/**
 * <br>Class Name: WebSecurityPathConfig
 * <br>Description: Login permission verification configuration
 * <p/>
 * <br>Created Time: February 23, 2018
 * <br>Version: 1.0
 * <br>
 * <br>History: (Version) Author Time Comments
 */
@Configuration
@ConfigurationProperties(prefix = "security-config.path")
@Data
public class WebSecurityPathConfig {

    public List<String> authenticationExclude;

    public List<String> authorizationExclude;

    public List<String> checkSubmitTokenExclude;
}
