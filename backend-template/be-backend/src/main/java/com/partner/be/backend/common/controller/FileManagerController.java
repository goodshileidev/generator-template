package com.partner.be.backend.common.controller;

import com.partner.be.common.AbstractApiController;
import com.partner.be.common.result.ServiceResult;
import com.partner.be.backend.common.domain.UploadFile;
import com.partner.be.backend.common.po.FileUploadResult;
import com.partner.be.backend.common.service.FileManagerService;
import com.partner.be.backend.common.HelperUploadConfig;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author hongliangzhang
 * <p>
 * <p>
 * TODO
 * 1. 添加文件 md5， 相同时是否只保存一份
 * 2. 添加访问次数与最新访问时间，方便后续删除
 */
@Slf4j
@Api("文件管理")
@RestController
@RequestMapping("/common/file-manager")
public class FileManagerController extends AbstractApiController {
    private FileManagerService fileManagerService;

    private HelperUploadConfig config;

    public FileManagerController(FileManagerService fileManagerService, HelperUploadConfig config) {
        this.fileManagerService = fileManagerService;
        this.config = config;
    }

    @PostMapping("create-file")
    public ServiceResult create(@RequestParam(value = "file") MultipartFile file, UploadFile uploadFile) {
        UploadFile up = fileManagerService.create(file, uploadFile);
        FileUploadResult fileUploadResult = new FileUploadResult(up);
        fileUploadResult.setDownloadUrl(config.getServerHost() + "/common/file-manager/get/" + up.getFileId());
        ServiceResult res = new ServiceResult(fileUploadResult);
        return res;
    }

    @PostMapping("create-files/{bizCategory}")
    public ServiceResult create(@RequestParam(value = "files") MultipartFile[] file, @PathVariable(value = "bizCategory", required = false) String bizCategory) {
        List<FileUploadResult> list = new ArrayList<>();

        for (MultipartFile multipartFile : file) {
            UploadFile uploadFile = new UploadFile();
            uploadFile.setBizCategory(bizCategory);
            fileManagerService.create(multipartFile, uploadFile);
            FileUploadResult fileUploadResult = new FileUploadResult(uploadFile);
            fileUploadResult.setDownloadUrl("/api/common/file-manager/get/" + uploadFile.getFileId());
            list.add(fileUploadResult);
        }
        ServiceResult res = new ServiceResult(list);
        return res;
    }


    @PostMapping("create-cut-image/{bizCategory}")
    public ServiceResult createBase64Image(@RequestBody Map<String, String> imageData, @PathVariable(value = "bizCategory", required = false) String bizCategory) {
        ServiceResult res;
        try {
            String base64Image = imageData.get("base64Image");
            String fileName = imageData.get("fileName");
            if (StringUtils.isEmpty(fileName)) {
                fileName = String.format("%s.png", UUID.randomUUID());
            }
            if (!StringUtils.isEmpty(base64Image)) {
                if (base64Image.startsWith("data:image") && base64Image.contains(",")) {
                    int commaIndex = base64Image.indexOf(',');
                    base64Image = base64Image.substring(commaIndex + 1);
                }

                String dateFormat = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
                String filePath = dateFormat + "/" + "default" + "/" + UUID.randomUUID().toString() + "-" + fileName;
                String fileAbsolutePath = config.getBaseDir() + "/" + filePath;


                File dest = new File(fileAbsolutePath);
                if (!dest.getParentFile().exists()) {
                    dest.getParentFile().mkdirs();
                }

                byte[] imageBytes = Base64Utils.decodeFromString(base64Image);
                Path destinationFile = Paths.get(fileAbsolutePath);
                Files.write(destinationFile, imageBytes);

                UploadFile upFile = new UploadFile();
                upFile.setFileName(fileName);
                upFile.setBizCategory(bizCategory);
                upFile.setMimeType("image/png");
                upFile.setFileType("image");
                upFile.setSize((long) imageBytes.length);
                upFile.setStorage("local");
                upFile.setFilePath(filePath);
                fileManagerService.save(upFile);


                FileUploadResult fileUploadResult = new FileUploadResult(upFile);
                fileUploadResult.setDownloadUrl(config.getServerHost() + "/common/file-manager/get/" + upFile.getFileId());

                res = new ServiceResult(fileUploadResult);

            } else {
                res = new ServiceResult("content is empty");
            }

        } catch (Exception e) {
            res = new ServiceResult(String.format("upload fail %s", e.getMessage()));
        }
        return res;
    }


    @GetMapping("/get/{fileId}")
    public ResponseEntity<?> getFile(@PathVariable Long fileId) {
        try {
            UploadFile uploadFile = fileManagerService.getById(fileId);
            if (uploadFile == null) {
                return ResponseEntity.notFound().build();
            }

            // 生成完整的文件路径 这里确保fileKey是相对于rootLocation的相对路径
            String basePath = config.getBaseDir();
            Path file = Paths.get(basePath).resolve(uploadFile.getFilePath());

            // 检查文件是否存在
            if (!Files.exists(file)) {
                return ResponseEntity.notFound().build();
            }

            // 获取文件的Resource
            Resource fileResource = new InputStreamResource(Files.newInputStream(file));


            // 获取文件名并对其进行 URL 编码
            String fileName = uploadFile.getFileName();
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString());


            // 准备HTTP头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(uploadFile.getMimeType()));
            headers.setContentLength(Files.size(file)); // 确保设置正确的Content-Length
            headers.setCacheControl(CacheControl.noCache().getHeaderValue()); // 设置缓存策略

            if (!canPreviewInBrowser(uploadFile.getMimeType())) {
                // 判断文件是否可以浏览器预览，如果不可以的，走下载
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFileName + "\"");
            }


            return ResponseEntity.ok()
                    .headers(headers)
                    .body(fileResource);
        } catch (Exception e) {
            log.error("get error fileId:{}", fileId);
            return ResponseEntity.unprocessableEntity().build();
//            return ResponseEntity.unprocessableEntity();
//            return ResponseEntity.internalServerError().build();
        }


    }

    // 判断是否可以在浏览器中预览的工具方法
    private boolean canPreviewInBrowser(String mimeType) {
        return mimeType.startsWith("image/") ||
                mimeType.equals("application/pdf") ||
                mimeType.startsWith("text/") ||
                mimeType.equals("application/json") ||
                mimeType.equals("application/xml");
    }
}
