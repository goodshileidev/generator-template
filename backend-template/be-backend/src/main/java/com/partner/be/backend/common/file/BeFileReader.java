package com.partner.be.backend.common.file;

import com.partner.be.backend.common.HelperUploadConfig;
import com.partner.be.backend.common.domain.UploadFile;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author hongliangzhang
 */
@Service
public class BeFileReader {
    private FileTextReaderService textReaderService;

    HelperUploadConfig config;

    public BeFileReader(FileTextReaderService textReaderService, HelperUploadConfig config) {
        this.textReaderService = textReaderService;
        this.config = config;
    }

    public String readFile(UploadFile uploadFile) {
        if (uploadFile == null) {
            return "";
        }
        String fileType = uploadFile.getFileType();
        Set<String> txtFileType = Arrays.stream(new String[]{"txt", "md", "sql"}).collect(Collectors.toSet());
        if (txtFileType.contains(fileType)) {
            String filePath = config.getBaseDir() + "/" + uploadFile.getFilePath();
            return textReaderService.readFile(filePath);
        }
        return "";
    }
}
