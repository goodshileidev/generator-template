package com.partner.be.common.db;

import java.util.Date;

/**
 * Interface defining creator audit fields
 */
public interface HasCreator {
    void setCreatedBy(String userId);
    void setCreatedAt(Date createdAt);
}
