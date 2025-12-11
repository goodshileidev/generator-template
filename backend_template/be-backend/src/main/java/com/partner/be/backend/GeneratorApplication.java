package com.partner.be.backend;


import com.partner.be.common.db.SqlOutInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.hystrix.security.HystrixSecurityAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Slf4j
@EnableCaching
@Configuration
@EnableAsync
@EnableScheduling
@EnableWebMvc
@AutoConfigureOrder(0)
@EnableTransactionManagement
@SpringBootApplication(scanBasePackages = {"com.partner.be", "com.partner.helper"}, exclude = {DataSourceAutoConfiguration.class,
        HystrixSecurityAutoConfiguration.class, SecurityAutoConfiguration.class, SecurityFilterAutoConfiguration.class
})
@Import({SqlOutInterceptor.class})
public class GeneratorApplication {

    public static void main(String[] args) {
        log.info("启动中。");
        SpringApplication.run(GeneratorApplication.class, args);
        log.info("启动成功。 ");
    }


}
