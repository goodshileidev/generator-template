package com.partner.be.common.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;


/**
 * <br>クラス名: AuthorizationInterceptor
 * <br>説　明: 権限チェック
* <br>作成時間： 2018年10月19日 上午11:40:15
 * <br>版 本： 1.0
 * <br>
 * <br>履　歴: (版本) 作者 时间 注释
 */


@Slf4j
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {

    List<String> authorizationExclude;

    public AuthorizationInterceptor(List<String> authorizationExclude) {
        this.authorizationExclude = authorizationExclude;
    }

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                             Object handler) throws Exception {
        String path = httpServletRequest.getServletPath();
        for (String pathRegex : authorizationExclude) {
            if (path.matches(pathRegex)) {
                return true;
            }
        }
        boolean succeed = true;
        if (handler instanceof HandlerMethod) {
            boolean hasPermission = false;
            String requestUri = httpServletRequest.getRequestURI();
            String contextPath = httpServletRequest.getContextPath();
            String url = requestUri.substring(contextPath.length());
            String method = httpServletRequest.getMethod();
            if (!hasPermission) {
                // 提示用户没权限
                httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
                httpServletResponse.setContentType("application/json; charset=UTF-8");
                PrintWriter out = httpServletResponse.getWriter();

                out.write("{\"code\":710,\"message\":\"権限ありません!\"}");
                out.flush();
                out.close();
                succeed = false;
            }
        }
        return succeed;
    }
}
