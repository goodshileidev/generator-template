package com.partner.be.common.filter;

import com.partner.be.common.ApiConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;

/**
 * <br>クラス名: AuthenticationInterceptor
 * <br>説　明: ログイン済みチェック
* <br>作成時間： 2018年10月19日 上午11:40:15
 * <br>版 本： 1.0
 * <br>
 * <br>履　歴: (版本) 作者 时间 注释
 */

@Slf4j
public class AuthenticationInterceptor extends HandlerInterceptorAdapter {
    List<String> authenticateExclude;

    public AuthenticationInterceptor(List<String> authenticateExclude) {
        this.authenticateExclude = authenticateExclude;
    }

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                             Object handler) throws Exception {
        String path = httpServletRequest.getServletPath();
        for (String pathRegex : authenticateExclude) {
            if (path.matches(pathRegex)) {
                return true;
            }
        }
        boolean succeed = true;
        if (handler instanceof HandlerMethod) {
            if (httpServletRequest.getSession().getAttribute(ApiConstants.Fields.USER_VALUE_OBJECT_KEY) == null
                    || httpServletRequest.getSession(false) == null) {
                // 没登录就要求登录
                httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
                httpServletResponse.setContentType("application/json; charset=UTF-8");
                PrintWriter out = httpServletResponse.getWriter();
                out.write("{\"code\":\"400\",\"message\":\"ログインください!\"}");
                out.flush();
                out.close();
                succeed = false;
            }
        }
        return succeed;
    }
}
