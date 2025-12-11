package com.partner.be.common;

import com.partner.be.common.db.HasCreator;
import com.partner.be.common.db.HasUpdater;
import lombok.Data;

import java.util.Date;

/**
 * Base domain object with audit fields
 */
@Data
public class BaseDomain implements HasCreator, HasUpdater {
    protected String createdBy;
    protected String updatedBy;
    protected int isDeleted;
    protected Date deletedAt;
    protected Date createdAt;
    protected Date updatedAt;
    protected Date dataDate;
    protected Integer requestId;
}
