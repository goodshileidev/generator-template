package com.partner.be.common;


import com.partner.be.common.db.HasCreator;
import com.partner.be.common.db.HasDataDate;
import com.partner.be.common.db.HasUpdater;
import com.partner.be.common.db.an.CreateTime;
import com.partner.be.common.db.an.UpdateTime;
import lombok.Data;

import java.util.Date;

/**
 * @author hongliangzhang
 */
@Data
public class BaseDomain implements HasDataDate, HasCreator, HasUpdater {
    protected String createdBy;
    protected String updatedBy;
    protected int isDeleted;

    protected Date deletedAt;
    @CreateTime
    protected Date createdAt;
    @UpdateTime
    protected Date updatedAt;
    protected Date dataDate;
    protected Integer requestId;

}
