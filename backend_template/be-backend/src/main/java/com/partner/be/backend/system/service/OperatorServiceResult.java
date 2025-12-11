package com.partner.be.backend.system.service;

import com.partner.be.common.result.ServiceResult;
import com.partner.be.common.result.ServiceResultType;

/**
 * Operator Service Result
 *
 * This class defines service result types for operator-related operations,
 * including error codes and messages for user management scenarios.
 *
 * @author System
 */
public class OperatorServiceResult<Operator> extends ServiceResult<Operator> {
  /** Login name already exists */
  public static final ServiceResultType LOGIN_NAME_EXIST =
      new ServiceResultType("LOGIN_NAME_EXIST", "operator.login.name.exist");

  /** User does not exist */
  public static final ServiceResultType USER_NO_EXIST =
      new ServiceResultType("OPERATOR_NO_EXIST", "operator.no.exist");

  /** Username or password is incorrect */
  public static final ServiceResultType USER_PASSWORD_ERROR =
      new ServiceResultType("NAME_PASSWORD_ERROR", "operator.name_password.error");

  public static final ServiceResultType PASSWORD_ERROR =
      new ServiceResultType("PASSWORD_ERROR", "operator.password.error");

  /** Password reset required (legacy password migration) */
  public static final ServiceResultType PASSWORD_RESET_REQUIRED =
      new ServiceResultType("PASSWORD_RESET_REQUIRED", "operator.password.reset.required");

  public OperatorServiceResult() {}

  public OperatorServiceResult(ServiceResultType resultType, Operator operator) {
    super(resultType, operator);
  }

  public OperatorServiceResult(ServiceResultType resultType) {
    super(resultType);
  }
}
