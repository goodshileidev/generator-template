package com.partner.be.common.exception;

import lombok.Data;
/**
 * <br>クラス名: BusinessLogicException
 * <br>説　明: 業務処理異常
* <br>作成時間： 2018年10月19日 上午11:40:15
 * <br>版 本： 1.0
 * <br>
 * <br>履　歴: (版本) 作者 时间 注释
 */

@Data
public class BusinessLogicException extends ServiceException {

    private Integer code;

    private String params;

    private Exception exception;

    public BusinessLogicException() {
        super();
    }

    public BusinessLogicException(String message) {
        super(message);
    }

    public BusinessLogicException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessLogicException(Integer code, String message, String params) {
        super(message);
        this.code = code;
        this.params = params;
    }

    public BusinessLogicException(Integer code, String message, String params, Exception exception) {
        super(message);
        this.code = code;
        this.params = params;
        this.exception = exception;
    }
}
