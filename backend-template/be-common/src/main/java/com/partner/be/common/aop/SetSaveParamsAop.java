package com.partner.be.common.aop;


import com.partner.be.common.db.HasCreator;
import com.partner.be.common.db.HasDataDate;
import com.partner.be.common.db.HasUpdater;
import com.partner.be.common.filter.UserThreadHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
@Aspect
@Slf4j
public class SetSaveParamsAop {

    @Pointcut("execution(public * com.partner.be.*.service.*Impl.save(..))")
    public void savePoint() {
    }

    @Pointcut("execution(public * com.partner.be.*.service.*Impl.create(..))")
    public void createPoint() {
    }

    @Pointcut("createPoint() || savePoint()")
    public void allPoint() {

    }

    /**
     * データの保存する場合、共通用パラメーターを設定する
     *
     * @param
     * @return
     */
    @Around("createPoint()")
    public Object beforeCreate(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        for (int i = 0; i < args.length; ++i) {
            if (args[i] instanceof HasDataDate) {
                if (((HasDataDate) args[i]).getDataDate() == null) {
                    ((HasDataDate) args[i]).setDataDate(new Date());
                }
            }
        }
        return pjp.proceed();
    }

    /**
     * <br>説　明: 拦截controller层 如果返回值为ServiceResult重新封装数据
    * <br>履　歴: (版本) 作者 时间 注释
     *
     * @param
     * @return
     */
    @Around("allPoint()")
    public Object beforeSave(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        Date createTime = new Date();
        for (int i = 0; i < args.length; ++i) {
            if (args[i] instanceof HasCreator) {
                ((HasCreator) args[i]).setCreatedBy((String) UserThreadHolder.getLoginUser().getUserId());
                ((HasCreator) args[i]).setCreatedAt(createTime);
            }
            if (args[i] instanceof HasUpdater) {
                ((HasUpdater) args[i]).setUpdatedBy((String) UserThreadHolder.getLoginUser().getUserId());
                ((HasUpdater) args[i]).setUpdatedAt(createTime);
            }

        }
        return pjp.proceed();
    }

}
