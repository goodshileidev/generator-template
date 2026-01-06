package com.partner.be.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CORS Configuration
 *
 * Configures Cross-Origin Resource Sharing (CORS) to allow specific trusted origins
 * to access the API. This prevents CSRF attacks by restricting which domains can
 * make authenticated requests.
 *
 * REQ-SEC-CORS: Secure CORS configuration
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                    "https://ems-lite.ene-cloud.com",
                    "http://localhost:3000",
                    "http://localhost:9001",
                    "http://127.0.0.1:3000",
                    "http://127.0.0.1:9001"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("Authorization", "Content-Type", "X-Requested-With")
                .exposedHeaders("Authorization")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
