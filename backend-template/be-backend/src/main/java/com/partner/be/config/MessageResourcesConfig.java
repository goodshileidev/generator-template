package com.partner.be.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.List;

/**
 * <br>クラス名: MessageResoucesConfig
 * <br>説　明: 资源文件配置
 * <p/>
 * <br>作成時間: 2018年11月11日
 * <br>版 本: 1.0
 * <br>
 * <br>履　歴: (版本) 作者 时间 注释
 */
@Configuration
@ConfigurationProperties(prefix = "spring.messages")
@Slf4j
public class MessageResourcesConfig {

    private List<String> baseNameList;

    @Bean(name = "messageSource")
    public ResourceBundleMessageSource getMessageResource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        if (baseNameList == null) {
            log.info("未配置message资源文件");
            return null;
        }
        String[] strings = new String[baseNameList.size()];
        messageSource.setBasenames(baseNameList.toArray(strings));
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    public List<String> getBasenameList() {
        return baseNameList;
    }

    public void setBasenameList(List<String> baseNameList) {
        this.baseNameList = baseNameList;
    }
}
