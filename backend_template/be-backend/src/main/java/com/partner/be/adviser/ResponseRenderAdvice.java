package com.partner.be.adviser;

import com.partner.be.common.http.result.R;
import com.partner.be.common.result.ServiceResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;


/**
 * JSON形のレスポンスを作成
 */

@ResponseStatus(HttpStatus.OK)
@ControllerAdvice(basePackages = "com.partner")
public class ResponseRenderAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {

        if (body == null) {
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            // 业务代码无返回数据
            return R.success("");
        }

        String bodyClassName = body.getClass().getName();

        if (StringUtils.startsWith(bodyClassName, "org.springframework.core")) {
            return body;
        }

        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        if (body instanceof R) {
            return body;
        } else if (body instanceof String) {
            return R.success((String) body);
        } else if (body instanceof ServiceResult) {
            return R.data(((ServiceResult<?>) body).getResult(), ((ServiceResult<?>) body).getResultMessage());
        } else {
            // 业务代码有返回数据
            return R.data(body);
        }
    }
}
