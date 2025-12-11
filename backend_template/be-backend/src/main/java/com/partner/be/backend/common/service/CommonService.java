package com.partner.be.backend.common.service;

import com.partner.be.backend.common.po.LockPO;

public interface CommonService {
    /**
     * 锁定数据
     *
     * @param lockPO
     * @return
     */
    CommonServiceResult lock(LockPO lockPO);

    /**
     * 锁定数据
     *
     * @param lockPO
     * @return
     */
    CommonServiceResult unlock(LockPO lockPO);
}
