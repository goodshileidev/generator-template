package com.partner.be.common.filter;

import com.partner.be.common.ApiConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;
/**
 * <br>クラス名: CheckSubmitTokenInterceptor
 * <br>説　明: SubmitTokenチェック
* <br>作成時間： 2018年10月19日 上午11:40:15
 * <br>版 本： 1.0
 * <br>
 * <br>履　歴: (版本) 作者 时间 注释
 */

public class CheckSubmitTokenInterceptor extends HandlerInterceptorAdapter {
    List<String> checkSubmitTokenExclude;

    public CheckSubmitTokenInterceptor(List<String> checkSubmitTokenExclude) {
        this.checkSubmitTokenExclude = checkSubmitTokenExclude;
    }

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                             Object handler) throws Exception {
        String path = httpServletRequest.getServletPath();
        for (String pathRegex : checkSubmitTokenExclude) {
            if (path.matches(pathRegex)) {
//                return true;
            }
        }
        boolean succeed = true;
        if (//System.getProperty("check_submit_token") != null &&
                ("PUT".equalsIgnoreCase(httpServletRequest.getMethod())
                        || "POST".equalsIgnoreCase(httpServletRequest.getMethod())
                        || "DELETE".equalsIgnoreCase(httpServletRequest.getMethod()))) {
            String sessionToken = (String) httpServletRequest.getSession().getAttribute(ApiConstants.Fields.SUBMIT_TOKEN_NAME);
            String headerToken = (String) httpServletRequest.getSession().getAttribute(ApiConstants.Fields.SUBMIT_TOKEN_NAME);
            if (!StringUtils.equalsIgnoreCase(headerToken, sessionToken)) {
                httpServletResponse.setContentType("application/json");
                httpServletResponse.setCharacterEncoding("utf-8");
                httpServletResponse.getWriter().write("{\"" +
                        "\"submitToken\":\"" + createSystemSubmitToken(httpServletRequest) + "\"," +
                        "\"code\":400, \n" +
                        "\"message\":\"Token正しいではありません\"}");
                httpServletResponse.getWriter().flush();
                succeed = false;
            } else {
                createSystemSubmitToken(httpServletRequest);
            }
        }
        return succeed;
    }

    /**
     * 新トーコンを作成
     *
     * @param request
     * @return
     */
    String createSystemSubmitToken(HttpServletRequest request) {
        String newToken = UUID.randomUUID().toString().replaceAll("-", "");
        request.getSession().setAttribute(ApiConstants.Fields.SUBMIT_TOKEN_NAME, newToken);
        UserThreadHolder.set(ApiConstants.Fields.SUBMIT_TOKEN_NAME, newToken);
        return newToken;
    }
}
