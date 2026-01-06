package com.partner.be.common.aop;


import com.partner.be.common.db.PageParam;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class SetPageParamsAop {


    @Pointcut("execution(public * com.partner.be.*.service.*Impl.search(..))")
    public void servicePagePoint() {
    }

    @Pointcut("execution(public * com.partner.be.*.controller.*.search*(..))")
    public void controllerPagePoint() {
    }

    @Pointcut("servicePagePoint() || controllerPagePoint()")
    public void pagePoint() {

    }

    /**
     * ページング用パラメーターを設定する
     *
     * @param
     * @return
     */
    @Around("pagePoint()")
    public Object beforePage(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        for (int i = 0; i < args.length; ++i) {
            if (args[i] instanceof PageParam) {
                PageParam pageParam = (PageParam) args[i];
//                if (pageParam instanceof HasNhfpcPathPrefix) {
//                    UserThreadHolder.setNhfpcParams(pageParam);
//                }
//                if (pageParam.getParams() instanceof HasRootNhfpcId) {
//                    UserThreadHolder.setRootNhfpcId((HasRootNhfpcId) pageParam.getParams());
//                }
            }
        }
        return pjp.proceed();
    }

}
