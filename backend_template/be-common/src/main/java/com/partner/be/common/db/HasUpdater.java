package com.partner.be.common.db;

import java.util.Date;

/**
 * Interface defining updater audit fields
 */
public interface HasUpdater {
    void setUpdatedBy(String userId);
    void setUpdatedAt(Date updatedAt);
}
