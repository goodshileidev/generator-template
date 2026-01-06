package com.partner.be.backend.common.po;

import lombok.Data;

import java.util.Date;

@Data
public class LockPO {
    String categoryName;
    String dataId;
    String isLocked;
    Date lockedAt;
}
