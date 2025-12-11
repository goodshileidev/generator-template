package com.partner.be.backend.common.service;

import com.partner.be.common.result.ServiceResult;
import com.partner.be.common.result.ServiceResultType;

/**
 * 代码定义
 *
 * @author ${author}
 * @email ${email}
 * @date ${datetime}
 */
public class CommonServiceResult<CodeEnum> extends ServiceResult<CodeEnum> {

    public CommonServiceResult() {

    }

    public CommonServiceResult(ServiceResultType resultType, CodeEnum codeEnum) {
        super(resultType, codeEnum);
    }

    public CommonServiceResult(ServiceResultType resultType) {
        super(resultType);
    }

}
