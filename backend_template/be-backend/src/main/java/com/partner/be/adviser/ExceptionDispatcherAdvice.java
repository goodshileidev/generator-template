package com.partner.be.adviser;

import com.partner.be.common.http.result.R;
import com.partner.be.common.http.result.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice(basePackages = "com.partner")
@ResponseStatus(HttpStatus.OK)
public class ExceptionDispatcherAdvice {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Object handleThrowableException(MethodArgumentNotValidException exception) {
        List<String> messages = exception.getBindingResult().getFieldErrors().stream().map(error -> error.getDefaultMessage()).collect(Collectors.toList());
        return R.fail(ResultCode.PARAM_ERROR.getCode(), StringUtils.join(messages, ","));
    }

    @ExceptionHandler({NullPointerException.class})
    @ResponseBody
    public Object handlingNullPointException(NullPointerException exception) {
        return R.fail(ResultCode.SERVER_ERROR.getCode(), StringUtils.join(ExceptionUtils.getRootCauseStackTrace(exception), "\n"));
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Object handleException(Exception exception) {
        exception.printStackTrace();
        return R.fail(ResultCode.SERVER_ERROR.getCode(), ExceptionUtils.getStackTrace(exception));
    }

    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public Object handleThrowableException(Throwable exception) {
        return R.fail(ResultCode.SERVER_ERROR.getCode(), ExceptionUtils.getStackTrace(exception));
    }

}
