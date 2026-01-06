package com.partner.be.backend.common.po;

import com.partner.be.backend.common.domain.UploadFile;
import lombok.Data;
import org.springframework.beans.BeanUtils;


/**
 * @author hongliangzhang
 */
@Data
public class FileUploadResult extends UploadFile {
    private String downloadUrl;

    public FileUploadResult() {

    }

    public FileUploadResult(UploadFile uploadFile) {
        BeanUtils.copyProperties(uploadFile, this);
    }
}
