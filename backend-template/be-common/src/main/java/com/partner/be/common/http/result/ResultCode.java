package com.partner.be.common.http.result;

import lombok.Getter;

/**
 * @desc 状态码
 */
@Getter
public enum ResultCode {
    /**
     * 処理成功
     */
    SUCCESS(200, "処理成功"),
    /**
     * リクエスト先存在しない
     */
    REQUEST_ADDRESS_NOT_EXIST(404, "リクエスト先存在しない"),
    /**
     * 処理異常
     */
    SERVER_ERROR(500, "処理異常"),
    /**
     * パラメータエラー
     */
    INVALID_PARAM(600, "パラメータエラー"),
    /**
     * パラメータマッチング異常
     */
    PARAM_TYPE_ERROR(602, "パラメータマッチング異常"),
    /**
     * SQL異常
     */
    SQL_ERROR(706, "SQL異常"),
    /**
     * 権限異常
     */
    AUTH_ERROR(710, "権限異常"),
    /**
     * 未定義異常
     */
    UNKNOWN_EXCEPTION(10000, "未定義異常"),
    /**
     * パラメータエラー
     */
    PARAM_ERROR(400, "パラメータエラー"),
    /**
     * 未知パラメータ
     */
    UNKNOWN_PARAM(603, "未知パラメータ"),
    ;


    /**
     * 結果コード
     */
    private Integer code;
    /**
     * メッセージ
     */
    private String message;

    ResultCode(Integer code, String msg) {
        this.code = code;
        this.message = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
