package com.partner.be.common.result;

import lombok.Data;

/**
 * Service Result Type definitions
 *
 * This class defines standard service result types used throughout the application
 * to represent the outcome of service operations. It provides predefined constants
 * for common result scenarios like success, failure, and validation errors.
 *
 * Class Name: ServiceResultType
 * Description: Defines service result types
 * Created: January 12, 2018
 * Version: 1.0
 *
 * History: (Version) Author Time Comments
 */
@Data
public class ServiceResultType {

    /**
     * Operation successful
     */
    public static final ServiceResultType SUCCESS = new ServiceResultType("SUCCESS", "success");

    /**
     * Operation failed
     */
    public static final ServiceResultType FAILED = new ServiceResultType("FAILED", "failed");

    /**
     * Validation error
     */
    public static final ServiceResultType VALIDATION = new ServiceResultType("VALIDATED_EXCEPTION", "validated.error.message");


    /**
     * Result message
     */
    String message;

    /**
     * Result code
     */
    String code;

    /**
     * Default constructor
     */
    ServiceResultType() {
    }

    /**
     * Constructor with code and message
     *
     * @param code The result code
     * @param message The result message
     */
    public ServiceResultType(String code, String message) {
        this.message = message;
        this.code = code;
    }

}
