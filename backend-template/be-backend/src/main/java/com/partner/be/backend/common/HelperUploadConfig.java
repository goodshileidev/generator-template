package com.partner.be.backend.common;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.File;

@Configuration
@ConfigurationProperties(prefix = "upload")
@Slf4j
@Data
public class HelperUploadConfig {
    private String baseDir;
    private String serverHost;
    private String category; // 上传类型  默认 local

    @PostConstruct
    public void fixBaseDirRelativePath() {
        if (StringUtils.startsWith(baseDir, "./")) {
            File file = new File(baseDir);
            String absolutePath = file.getAbsolutePath();
            String newPath = StringUtils.remove(absolutePath, "./");
            if (!file.exists()) {
                file.mkdirs();
            }

            setBaseDir(newPath);
            log.info("fixBasedir Relative path:{}", newPath);
        }
    }
}
