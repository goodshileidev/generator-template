package com.partner.be.backend.common.file;

import cn.hutool.core.io.IoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;

/**
 * @author hongliangzhang
 */
@Slf4j
@Service
public class FileTextReaderService {
    public String readFile(String path) {
        File file = new File(path);
        if (file.exists() && file.isFile()) {
            try {
                String res = IoUtil.read(new FileReader(file));
                return res;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return "";
    }
}
