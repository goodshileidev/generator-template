package com.partner.be.common.db;

import com.partner.be.common.util.ReflectUtils;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Plugin;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Properties;


public abstract class AbstractInterceptor implements Interceptor {
    /**
     * 返回RoutingStatementHandler。
     *
     * @param target
     * @return
     */
    protected Object getWrappedObject(Object target) {
        while (target instanceof Proxy) {
            Proxy proxy = (Proxy) target;
            InvocationHandler ih = proxy.getInvocationHandler(proxy);
            if (ih instanceof Plugin) {
                target = ReflectUtils.getFieldValue(ih, "target");
            } else {
                target = proxy;
            }
        }
        return target;
    }

    /**
     * 拦截器对应的封装原始对象的方法
     */
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
