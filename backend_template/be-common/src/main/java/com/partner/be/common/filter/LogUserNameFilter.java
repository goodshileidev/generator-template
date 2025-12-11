package com.partner.be.common.filter;

import org.slf4j.MDC;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 説　明　:<br>
 * <p>
 * ユザー名をログに出力できるようにする
 * <p/>
 * <br>
 * 作　者: ${author} <br>
 * 作成時間: 2018年02月28日 <br>
 * 履　歴: (版本) 作者 时间 注释
 */
public class LogUserNameFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        try {
            StringBuilder sb = new StringBuilder();
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            if (httpServletRequest.getHeader("loginName") != null) {
                sb.append(httpServletRequest.getHeader("loginName"));
            }
            if (httpServletRequest.getHeader("userId") != null) {
                sb.append("@").append(httpServletRequest.getHeader("userId"));
            }
            MDC.put("userToken", sb.toString());
            chain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }
}
