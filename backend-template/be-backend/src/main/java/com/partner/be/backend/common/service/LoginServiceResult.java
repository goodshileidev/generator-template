package com.partner.be.backend.common.service;

import com.partner.be.common.result.ServiceResult;
import com.partner.be.common.result.ServiceResultType;

/**
 * Code Definition
 *
 * @author ${author}
 * @email ${email}
 * @date ${datetime}
 */
import lombok.Getter;
import lombok.Setter;

public class LoginServiceResult<OperatorVO> extends ServiceResult<OperatorVO> {

  public static final ServiceResultType VERIFY_CODE_ERROR =
      new ServiceResultType("VERIFY_CODE_ERROR", "account.verify_code.error");

  public static final ServiceResultType VERIFY_CODE_EXPIRED =
      new ServiceResultType("VERIFY_CODE_EXPIRED", "account.verify_code.expired");

  public static final ServiceResultType PASSWORD_REQUIRED =
      new ServiceResultType("PASSWORD_REQUIRED", "account.password.required");

  public static final ServiceResultType PASSWORD_CONFIRM_ERROR =
      new ServiceResultType("PASSWORD_CONFIRM_ERROR", "account.password.confirm_error");

  /** JWT token returned after successful authentication. */
  @Getter @Setter private String token;

  public LoginServiceResult() {}

  public LoginServiceResult(ServiceResultType resultType, OperatorVO operatorVO) {
    super(resultType, operatorVO);
  }

  public LoginServiceResult(ServiceResultType resultType) {
    super(resultType);
  }
}
