package com.partner.be.backend.common.service.impl;

import com.partner.be.backend.common.dao.CommonDao;
import com.partner.be.backend.common.po.LockPO;
import com.partner.be.backend.common.service.CommonService;
import com.partner.be.backend.common.service.CommonServiceResult;
import com.partner.be.common.result.ServiceResultType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class CommonServiceImpl implements CommonService {

    @Autowired
    CommonDao commonDao;

    @Override
    public CommonServiceResult lock(LockPO lockPO) {
        lockPO.setIsLocked("1");
        lockPO.setLockedAt(new Date());
        if (commonDao.updateLock(lockPO) > 0) {
            return new CommonServiceResult(ServiceResultType.SUCCESS);
        } else {
            return new CommonServiceResult(ServiceResultType.FAILED);
        }
    }

    @Override
    public CommonServiceResult unlock(LockPO lockPO) {
        lockPO.setIsLocked("0");
        lockPO.setLockedAt(new Date());
        if (commonDao.updateLock(lockPO) > 0) {
            return new CommonServiceResult(ServiceResultType.SUCCESS);
        } else {
            return new CommonServiceResult(ServiceResultType.FAILED);
        }
    }
}
