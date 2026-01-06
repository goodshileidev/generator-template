package com.partner.be.common.system.po;

import com.partner.be.common.BaseSearchPO;
import com.partner.be.common.HasCompanyIdCondition;
import com.partner.be.common.db.SearchParam;
import java.util.List;
import lombok.Data;

/**
 * User
 *
 * @author ${author}
 * @email ${email}
 * @date ${datetime}
 */
@Data
public class OperatorSearchPO extends BaseSearchPO implements SearchParam, HasCompanyIdCondition {

  private static final long serialVersionUID = 1L;

  /** Power Station Name */
  protected String powerStationNameCondition;

  /** Company Name */
  protected String companyNameCondition;

  /** Company Information ID */
  protected String companyIdCondition;

  /** Usage status */
  protected String usageStatusCondition;

  /** Associated mobile verification status */
  protected String associatedMobileVerificationStatusCondition;

  /** Associated email verification status */
  protected String associatedEmailVerificationStatusCondition;

  /** First Name */
  protected String firstNameCondition;

  /** Last Name */
  protected String lastNameCondition;

  /** Username */
  protected String usernameCondition;

  /** User role */
  protected String userRoleCondition;

  /** Email */
  protected String emailCondition;

  /** Power Station */
  protected String powerStationIdsCondition;

  /** Login welcome message settings status */
  protected String loginWelcomeMessageSettingStatusCondition;

  /** Position */
  protected String positionTitleCondition;

  protected List<String> companyIdListCondition;

  /** Language preference */
  protected String language;
}
