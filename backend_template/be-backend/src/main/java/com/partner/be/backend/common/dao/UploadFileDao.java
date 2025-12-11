package com.partner.be.backend.common.dao;

import com.partner.be.backend.common.domain.UploadFile;

/**
 * @author hongliangzhang
 */
public interface UploadFileDao {
    /**
     * 插入数据
     */
    long insert(UploadFile uploadFile);

    /**
     * 查询单条数据
     */
    UploadFile getById(Long id);
}
