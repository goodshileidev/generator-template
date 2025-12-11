package com.partner.be.common.system.vo;

import com.partner.be.common.system.domain.Operator;
import com.partner.be.common.db.ResultObject;
import lombok.Data;

/**
 * User
 *
 * @author ${author}
 * @email ${email}
 * @date ${datetime}
 */
@Data
public class OperatorVO extends Operator implements ResultObject {

  private static final long serialVersionUID = 1L;

  /** Power Station name */
  protected String powerStationName;

  /** Company code */
  protected String companyCode;

  /** Company name */
  protected String companyName;

  /** Usage status */
  protected String usageStatusTitle;

  /** Associated mobile verification status */
  protected String associatedPhoneNumberVerificationStatusTitle;

  /** Associated email verification status */
  protected String associatedEmailVerificationStatusTitle;

  /** User role */
  protected String userRoleTitle;

  /** Login welcome message settings status */
  protected String loginWelcomeMessageSettingStatusTitle;
}
