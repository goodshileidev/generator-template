package com.partner.be.backend.common.domain;

import com.partner.be.common.BaseDomain;
import com.partner.be.common.db.HasCreator;
import com.partner.be.common.db.HasUpdater;
import lombok.Data;

import java.io.Serializable;

/**
 * @author hongliangzhang
 */
@Data
public class UploadFile extends BaseDomain implements Serializable, HasCreator, HasUpdater {
    private static final long serialVersionUID = 1L;
    /**
     * 实体id
     */
    protected Long fileId;
    private String fileName;
    private String fileType;
    private Long size;
    private String filePath;
    private String bizCategory;// 业务类型
    private String storage;
    private String mimeType;


}
