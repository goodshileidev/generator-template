package com.partner.be.backend.common.file;

import com.partner.be.backend.common.domain.UploadFile;
import com.partner.be.backend.common.HelperUploadConfig;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @author hongliangzhang
 */
@Service
public class LocalFileUploadService {
    HelperUploadConfig config;

    public LocalFileUploadService(HelperUploadConfig config) {
        this.config = config;
    }

    public UploadFile uploadToLocal(MultipartFile file, String category) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty.");
        }

        UploadFile res = new UploadFile();
        res.setFileName(file.getOriginalFilename());

        String fileType = StringUtils.substring(file.getOriginalFilename(), file.getOriginalFilename().lastIndexOf(".") + 1);

        String fileContentType = file.getContentType();
        fileContentType = StringUtils.defaultIfBlank(fileContentType, "application/octet-stream");
        res.setMimeType(fileContentType);
        res.setFileType(fileType);
        res.setSize(file.getSize());
        res.setStorage("local");
        String dateFormat = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
        String filePath = dateFormat + "/" + category + "/" + UUID.randomUUID() + "-" + file.getOriginalFilename();
        res.setBizCategory(category);
        res.setFilePath(filePath);
        try {
            String fileAbsolutePath = config.getBaseDir() + "/" + filePath;
            File dest = new File(fileAbsolutePath);
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
//            file.transferTo(dest);
            FileUtils.copyToFile(new ByteArrayInputStream(file.getBytes()), dest);
//            FileUtils.copyToFile(file,dest);
//            return filePath;
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file to local storage.", e);
        }

        return res;
    }
}
