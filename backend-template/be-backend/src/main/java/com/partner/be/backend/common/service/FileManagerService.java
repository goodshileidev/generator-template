package com.partner.be.backend.common.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.partner.be.backend.common.dao.UploadFileDao;
import com.partner.be.backend.common.domain.UploadFile;
import com.partner.be.backend.common.file.LocalFileUploadService;
import com.partner.be.backend.common.HelperUploadConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author hongliangzhang
 */
@Service
public class FileManagerService {
    UploadFileDao managerDao;
    LocalFileUploadService localFileUploadService;

    HelperUploadConfig uploadConfig;

    public FileManagerService(UploadFileDao managerDao, LocalFileUploadService localFileUploadService, HelperUploadConfig config) {
        this.managerDao = managerDao;
        this.localFileUploadService = localFileUploadService;
        this.uploadConfig = config;
    }

    public UploadFile create(MultipartFile file, UploadFile uploadFile) {
        String category = StringUtils.defaultIfBlank(uploadFile.getBizCategory(), "default");
        UploadFile upload = localFileUploadService.uploadToLocal(file, category);
        BeanUtil.copyProperties(upload, uploadFile, CopyOptions.create().ignoreNullValue());

        managerDao.insert(uploadFile);
//        if (result > 0) {
//            FileUploadResult res = new FileUploadResult(ServiceResultType.SUCCESS, uploadFile);
//            res.setDownloadUrl(uploadConfig.getServerHost() + "/common/file-manager/get" + uploadFile.getFileId());
//            return res;
//        } else {
//            return new FileUploadResult(ServiceResultType.FAILED, null);
//        }
        return uploadFile;
    }

    public UploadFile getById(Long fileId) {
        return this.managerDao.getById(fileId);
    }

    public UploadFile save(UploadFile uploadFile){
        managerDao.insert(uploadFile);
        return uploadFile;
    }
}
