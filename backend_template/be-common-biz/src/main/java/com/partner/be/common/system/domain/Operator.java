package com.partner.be.common.system.domain;

import com.partner.be.common.BaseDomain;
import com.partner.be.common.db.HasCompanyId;
import com.partner.be.common.db.HasCreator;
import com.partner.be.common.db.HasUpdater;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/** User */
@Data
public class Operator extends BaseDomain
    implements Serializable, HasCreator, HasUpdater, HasCompanyId {
  private static final long serialVersionUID = 1L;

  /** UserId */
  protected String operatorId;

  /** Company Information ID */
  protected String companyId;

  /** Session Auto Logout Wait Time */
  protected String sessionAutoLogoutWaitingTime;

  /** Usage status */
  protected String usageStatus;

  /** Company Name */
  protected String companyName;

  /** Associated Mobile Number */
  protected String associatedMobileNumber;

  /** Associated mobile verification status */
  protected String associatedPhoneNumberVerificationStatus;

  /** Associated email verification status */
  protected String associatedEmailVerificationStatus;

  /** First Name */
  protected String firstName;

  /** Last Name */
  protected String lastName;

  /** Password */
  protected String password;

  protected String associatedMobileVerificationStatus;

  /** Mobile Number */
  protected String mobile;

  /** Language */
  protected String language;

  /** Mobile Verification Code */
  protected String mobileVerificationCode;

  /** Mobile Verification Code Send Time */
  protected Date mobileVerificationCodeSendTime;

  /** Mobile Verification Code Verification Time */
  protected Date mobileVerificationCodeVerifiedTime;

  /** Description */
  protected String description;

  /** Username */
  protected String username;

  /** User role */
  protected String userRole;

  /** Email */
  protected String email;

  /** Power Station */
  protected String powerStationIds;

  /** Login Welcome Message */
  protected String loginWelcomeMessage;

  /** Login welcome message settings status */
  protected String loginWelcomeMessageSettingStatus;

  /** Position */
  protected String positionTitle;

  /** Email Verification Code */
  protected String emailVerificationCode;

  /** Email Verification Code Send Time */
  protected Date emailVerificationCodeSendTime;

  /** Email verification code verification time */
  protected Date emailVerificationCodeVerifiedTime;
}
