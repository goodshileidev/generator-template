package com.partner.be.common.system.po;

import com.partner.be.common.db.SearchParam;
import com.partner.be.common.system.domain.Operator;
import lombok.Data;

/**
 * Operator Plain Object (PO) for search operations
 *
 * This class extends the Operator domain object and implements SearchParam interface
 * to provide search capabilities for operator/user data. It includes additional fields
 * for password management and power station association, enabling comprehensive
 * user management functionality within the system.
 *
 * @author ${author}
 * @email ${email}
 * @date ${datetime}
 */
@Data
public class OperatorPO extends Operator implements SearchParam {

  private static final long serialVersionUID = 1L;

  /** Current password for password change validation */
  public String oldPassword;

  /** New password for password update operations */
  public String newPassword;

  /** Associated power station name for user context and filtering */
  public String powerStationName;

  /** Email verification code provided by the user when updating email */
  public String verifyCode;
}
