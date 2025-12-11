package com.partner.be.common.http.result;

import com.partner.be.common.ApiConstants;
import com.partner.be.common.filter.UserThreadHolder;
import lombok.Data;

import java.io.Serializable;

@Data
public class R<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean success;

    private int code;

    private T data;

    private String token = "token";

    private String message;

    private R(int code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.message = msg;
        this.success = ResultCode.SUCCESS.getCode() == code;
        this.token = (String) UserThreadHolder.get(ApiConstants.Fields.SUBMIT_TOKEN_NAME);
    }

    public R() {
    }

    public static <T> R<T> data(T data, String msg) {
        return data(200, data, msg);
    }

    public static <T> R<T> data(T data) {
        return data(200, data, "");
    }

    public static <T> R<T> data(int code, T data, String msg) {
        return new R(code, data, msg);
    }

    public static <T> R<T> success(String msg) {
        return new R(ResultCode.SUCCESS.getCode(), null, msg);
    }

    public static <T> R<T> fail(String msg) {
        return new R(ResultCode.SERVER_ERROR.getCode(), null, msg);
    }

    public static <T> R<T> fail(int code, String msg) {
        return new R(code, null, msg);
    }


    public String toString() {
        return "R(code=" + this.getCode() + ", success=" + this.isSuccess() + ", data=" + this.getData() + ", msg=" + this.getMessage() + ")";
    }
}
