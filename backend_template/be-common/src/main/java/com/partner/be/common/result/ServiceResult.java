/**
 * Project: Auto Purchase
 * File: ServiceResult.java
 */
package com.partner.be.common.result;


import lombok.Data;

/**
 * Class: ServiceResult
 * Description: Defines service processing results
 *
 * Created: November 12, 2018
 * Version: 1.0
 *
 * History: (Version) Author Time Comments
 */
@Data
public class ServiceResult<T> {


    public static ServiceResult SUCCESS = new ServiceResult(ServiceResultType.SUCCESS);


    public static ServiceResult FAILED = new ServiceResult(ServiceResultType.FAILED);

    public static ServiceResult VALIDATION = new ServiceResult(ServiceResultType.VALIDATION);

    /**
     * Result type
     */
    protected ServiceResultType resultType;
    /**
     * Result description
     */
    protected String  resultMessage;

    /**
     * Result data
     */
    protected T result;

    public ServiceResult() {

    }


    public ServiceResult(ServiceResultType resultType) {
        this(resultType, resultType.getMessage(), null);
    }

    public ServiceResult(T result) {
        this(ServiceResultType.SUCCESS, ServiceResultType.SUCCESS.getMessage(), result);
    }

    public ServiceResult(ServiceResultType resultType, T result) {
        this.resultType = resultType;
        this.resultMessage = resultType.getMessage();
        this.result = result;
    }

    public ServiceResult(ServiceResultType resultType, String message, T result) {
        this.resultType = resultType;
        this.resultMessage = message;
        this.result = result;
    }

    public boolean isSuccess() {
        return this.resultType != null
            && ServiceResultType.SUCCESS.getCode().equals(this.resultType.getCode());
    }

}
