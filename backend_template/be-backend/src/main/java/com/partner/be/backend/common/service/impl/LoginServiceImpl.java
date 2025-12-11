package com.partner.be.backend.common.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.partner.be.backend.common.dao.LoginDao;
import com.partner.be.backend.common.po.LoginPO;
import com.partner.be.backend.common.security.JwtTokenProvider;
import com.partner.be.backend.common.security.PasswordHashService;
import com.partner.be.backend.common.service.LoginService;
import com.partner.be.backend.common.service.LoginServiceResult;
import com.partner.be.backend.system.service.OperatorService;
import com.partner.be.backend.system.service.OperatorServiceResult;
import com.partner.be.common.codelist.YesNo;
import com.partner.be.common.filter.UserThreadHolder;
import com.partner.be.common.result.ServiceResultType;
import com.partner.be.common.system.po.OperatorPO;
import com.partner.be.common.system.vo.OperatorVO;
import com.partner.be.notification.email.EmailCreator;
import com.partner.be.notification.util.EmailUtil;
import com.partner.be.postgres.system.dao.OperatorDao;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LoginServiceImpl implements LoginService {

  @Autowired LoginDao loginDao;
  @Autowired OperatorDao operatorDao;
  @Autowired OperatorService operatorService;
  @Autowired EmailCreator emailCreator;
  @Autowired JwtTokenProvider jwtTokenProvider;
  @Autowired PasswordHashService passwordHashService;
  @Autowired com.partner.be.backend.common.security.RateLimitService rateLimitService;

  private static final SecureRandom SECURE_RANDOM = new SecureRandom();
  private static final long EMAIL_VERIFY_CODE_EXPIRATION_MINUTES = 24 * 60L;

  /**
   * User Login - Authenticates user and returns user information with JWT token.
   *
   * <p><b>Password Format:</b> The password in loginPO should be SHA256-hashed by the frontend
   * before transmission. This method compares the SHA256 password against BCrypt(SHA256) stored in
   * the database.
   *
   * <p><b>Rate Limiting:</b> Login attempts are rate-limited to prevent brute force attacks.
   * Maximum 5 attempts allowed within 15 minutes per username.
   *
   * <p><b>Legacy Password Migration:</b> Users with legacy MD5 passwords will fail authentication
   * and must reset their password through the password reset flow.
   *
   * <p><b>Security Note:</b> All authentication failures return the same generic error to prevent
   * username enumeration attacks.
   *
   * @param loginPO login parameters containing username and SHA256-hashed password
   * @return login result with JWT token if successful, error code otherwise
   */
  public LoginServiceResult login(LoginPO loginPO) {
    String username = loginPO.getUsername();

    // Check rate limiting
    if (!rateLimitService.allowLoginAttempt(username)) {
      log.warn("Rate limit exceeded for login attempt: username={}", username);
      return new LoginServiceResult(OperatorServiceResult.USER_PASSWORD_ERROR);
    }

    OperatorVO operatorVO = loginDao.getByUserName(username);
    if (operatorVO != null) {
      if (passwordHashService.matches(loginPO.getPassword(), operatorVO.getPassword())) {
        // Successful login - clear rate limit attempts
        rateLimitService.clearLoginAttempts(username);

        // Create JWT token before clearing password field
        String token = jwtTokenProvider.createToken(operatorVO);

        // Clear sensitive fields using common method
        clearSensitiveFields(operatorVO);

        LoginServiceResult serviceResult =
            new LoginServiceResult(ServiceResultType.SUCCESS, operatorVO);
        serviceResult.setToken(token);
        return serviceResult;
      } else {
        // Authentication failed - record failed attempt
        rateLimitService.recordFailedLogin(username);

        // Return generic error for all failure cases to prevent information leakage
        // Note: Legacy MD5 passwords will also fail here, which is correct behavior
        if (passwordHashService.needsMigration(operatorVO.getPassword())) {
          log.warn("User {} has legacy MD5 password. Password reset required.", username);
        }
        return new LoginServiceResult(OperatorServiceResult.USER_PASSWORD_ERROR);
      }
    } else {
      // User not found - record failed attempt and return generic error
      rateLimitService.recordFailedLogin(username);
      return new LoginServiceResult(OperatorServiceResult.USER_PASSWORD_ERROR);
    }
  }

  @Override
  public LoginServiceResult logout(LoginPO loginPO) {
    return new LoginServiceResult(ServiceResultType.SUCCESS);
  }

  /**
   * Update Password - Changes the current user's password.
   *
   * <p><b>Password Format:</b> Both oldPassword and newPassword in updatePO should be SHA256-hashed
   * by the frontend before transmission.
   *
   * @param updatePO update parameters containing username, SHA256-hashed old password, and
   *     SHA256-hashed new password
   * @return update result with success or error code
   */
  @Override
  public LoginServiceResult updatePassword(LoginPO updatePO) {
    OperatorPO operatorPO = new OperatorPO();
    operatorPO.setOperatorId((String) UserThreadHolder.get(UserThreadHolder.LOGIN_USER_ID));
    OperatorVO operatorVO = loginDao.getByUserName(updatePO.getUsername());
    if (operatorVO == null
        || !passwordHashService.matches(updatePO.getOldPassword(), operatorVO.getPassword())) {
      return new LoginServiceResult(OperatorServiceResult.PASSWORD_ERROR);
    }
    if (StringUtils.isBlank(updatePO.getNewPassword())) {
      return new LoginServiceResult(LoginServiceResult.PASSWORD_REQUIRED);
    }
    if (StringUtils.isNotBlank(updatePO.getConfirmPassword())
        && !StringUtils.equals(updatePO.getNewPassword(), updatePO.getConfirmPassword())) {
      return new LoginServiceResult(LoginServiceResult.PASSWORD_CONFIRM_ERROR);
    }
    operatorPO.setNewPassword(updatePO.getNewPassword());
    OperatorServiceResult operatorServiceResult = operatorService.update(operatorPO);
    return new LoginServiceResult(operatorServiceResult.getResultType());
  }
//
//  /**
//   * Sends a password reset email.
//   *
//   * <p><b>Rate Limiting:</b> Password reset requests are rate-limited to prevent abuse. Maximum 3
//   * requests allowed within 60 minutes per email address.
//   *
//   * <p><b>Security Note:</b> Returns generic success/failure to prevent email enumeration.
//   *
//   * @param loginPO login parameters containing email address
//   * @return service result indicating success or failure
//   */
//  @Override
//  @Deprecated
//  public LoginServiceResult sendResetPasswordMail(LoginPO loginPO) {
//    if (StringUtils.isBlank(loginPO.getEmail())) {
//      return new LoginServiceResult(ServiceResultType.FAILED);
//    }
//
//    String email = loginPO.getEmail();
//
//    // Check rate limiting
//    if (!rateLimitService.allowPasswordResetRequest(email)) {
//      log.warn("Rate limit exceeded for password reset request: email={}", email);
//      // Return success to prevent email enumeration
//      return new LoginServiceResult(ServiceResultType.SUCCESS);
//    }
//
//    OperatorPO query = new OperatorPO();
//    query.setIsDeleted(0);
//    query.setEmail(email);
//    OperatorVO existingOperator = operatorDao.getByPO(query);
//
//    if (existingOperator != null) {
//      // Record rate limit attempt
//      rateLimitService.recordPasswordResetRequest(email);
//
//      Map<String, String> context = Maps.newHashMap();
//      context.put("email", email);
//      context.put("language", loginPO.getLanguage());
//      List<OperatorVO> receiptList = Lists.newArrayList();
//      receiptList.add(existingOperator);
//      Email emailMessage =
//          emailCreator.create(
//              "account_reset_password", context, receiptList, loginPO.getLanguage());
//      EmailUtil.send(emailMessage);
//      return new LoginServiceResult(ServiceResultType.SUCCESS);
//    } else {
//      // Return success even if email doesn't exist to prevent enumeration
//      log.debug("Password reset requested for non-existent email: {}", email);
//      return new LoginServiceResult(ServiceResultType.SUCCESS);
//    }
//  }

  public static String generateCode() {
    return String.format("%06d", SECURE_RANDOM.nextInt(1_000_000));
  }

  /**
   * Sends an email verification code.
   *
   * <p><b>Rate Limiting:</b> Email verification requests are rate-limited to prevent abuse. Maximum
   * 3 requests allowed within 60 minutes per email address.
   *
   * <p><b>Security Note:</b> Returns generic success/failure to prevent email enumeration.
   *
   * @param loginPO login parameters containing email address
   * @return service result indicating success or failure
   */
  @Override
  public LoginServiceResult sendEmailVerifyCode(LoginPO loginPO) {
    if (StringUtils.isBlank(loginPO.getEmail())) {
      return new LoginServiceResult(ServiceResultType.FAILED);
    }

    String email = loginPO.getEmail();

    // Check rate limiting
    if (!rateLimitService.allowEmailVerificationRequest(email)) {
      log.warn("Rate limit exceeded for email verification request: email={}", email);
      // Return success to prevent email enumeration
      return new LoginServiceResult(ServiceResultType.SUCCESS);
    }

    String verifyCode = generateCode();
    Map<String, Object> context = Maps.newHashMap();
    List<OperatorVO> receiptList = Lists.newArrayList();
    OperatorVO operatorVO = loginDao.getByUserName(email);

    if (operatorVO != null) {
      // Record rate limit attempt
      rateLimitService.recordEmailVerificationRequest(email);

      OperatorPO updatePO = new OperatorPO();
      updatePO.setOperatorId(operatorVO.getOperatorId());
      updatePO.setEmailVerificationCode(verifyCode);
      updatePO.setEmailVerificationCodeSendTime(new Date());
      updatePO.setAssociatedEmailVerificationStatus(YesNo.no.getCode());
      operatorDao.update(updatePO);

      receiptList.add(operatorVO);
      context.put("code", verifyCode);
      context.put("username", email);
      context.put("email", email);
      context.put("receipt", operatorVO);
      context.put("expireMinutes", EMAIL_VERIFY_CODE_EXPIRATION_MINUTES);
      try {
        Email emailMessage =
            emailCreator.create(
                "account_send_email_verify_code", context, receiptList, loginPO.getLanguage());
        EmailUtil.send(emailMessage);
      } catch (Exception e) {
        log.error("Failed to send email verification code to {}: {}", email, e.getMessage());
        // Continue anyway - don't fail the entire operation if email fails
      }
      return new LoginServiceResult(ServiceResultType.SUCCESS);
    } else {
      // Return success even if email doesn't exist to prevent enumeration
      log.debug("Email verification requested for non-existent email: {}", email);
      return new LoginServiceResult(ServiceResultType.SUCCESS);
    }
  }

  /**
   * Reset Password - Resets user password using email verification code.
   *
   * <p><b>Password Format:</b> The newPassword in loginPO should be SHA256-hashed by the frontend
   * before transmission. This allows users with legacy MD5 passwords to migrate to the new
   * BCrypt(SHA256) format.
   *
   * @param loginPO reset parameters containing email, verification code, and SHA256-hashed new
   *     password
   * @return reset result with success or error code
   */
  @Override
  public LoginServiceResult resetPassword(LoginPO loginPO) {
    OperatorPO operatorPO = new OperatorPO();
    operatorPO.setIsDeleted(0);
    operatorPO.setEmail(loginPO.getEmail());
    OperatorVO operatorVO = operatorDao.getByPO(operatorPO);
    if (operatorVO != null) {
      if (StringUtils.isBlank(loginPO.getVerifyCode())) {
        return new LoginServiceResult(LoginServiceResult.VERIFY_CODE_ERROR);
      }
      if (!StringUtils.equalsIgnoreCase(
          operatorVO.getEmailVerificationCode(), loginPO.getVerifyCode())) {
        return new LoginServiceResult(LoginServiceResult.VERIFY_CODE_ERROR);
      }
      if (isVerifyCodeExpired(operatorVO.getEmailVerificationCodeSendTime())) {
        return new LoginServiceResult(LoginServiceResult.VERIFY_CODE_EXPIRED);
      }
      if (StringUtils.isBlank(loginPO.getNewPassword())) {
        return new LoginServiceResult(LoginServiceResult.PASSWORD_REQUIRED);
      }
      if (!StringUtils.equals(loginPO.getNewPassword(), loginPO.getConfirmPassword())) {
        return new LoginServiceResult(LoginServiceResult.PASSWORD_CONFIRM_ERROR);
      }
      OperatorPO updatePO = new OperatorPO();
      updatePO.setOperatorId(operatorVO.getOperatorId());
      updatePO.setEmailVerificationCodeVerifiedTime(new Date());
      updatePO.setEmailVerificationCode("");
      updatePO.setAssociatedEmailVerificationStatus(YesNo.yes.getCode());
      updatePO.setEmailVerificationCodeSendTime(new Date(0));
      updatePO.setPassword(passwordHashService.hash(loginPO.getNewPassword()));
      operatorDao.update(updatePO);

      List<OperatorVO> receiptList = Lists.newArrayList();
      receiptList.add(operatorVO);
      Map<String, Object> context = Maps.newHashMap();
      context.put("username", loginPO.getEmail());
      context.put("email", loginPO.getEmail());
      context.put("receipt", operatorVO);
      try {
        Email email =
            emailCreator.create(
                "account_password_been_reset", context, receiptList, loginPO.getLanguage());
        EmailUtil.send(email);
      } catch (Exception e) {
        log.error("Failed to send password reset confirmation email to {}: {}", loginPO.getEmail(), e.getMessage());
        // Continue anyway - password was successfully reset even if email fails
      }
      return new LoginServiceResult(ServiceResultType.SUCCESS);
    } else {
      return new LoginServiceResult(ServiceResultType.FAILED);
    }
  }

  private boolean isVerifyCodeExpired(Date sendTime) {
    if (sendTime == null) {
      return true;
    }
    Instant sent = sendTime.toInstant();
    Instant now = Instant.now();
    return Duration.between(sent, now).toMinutes() > EMAIL_VERIFY_CODE_EXPIRATION_MINUTES;
  }

  /**
   * Get Current User Information
   *
   * <p>Retrieves the current user's information with sensitive fields removed. This method uses the
   * same field-clearing logic as the login method to ensure consistency across all API responses.
   *
   * @param operatorId the operator ID from JWT token
   * @return operator information with sensitive fields cleared
   */
  @Override
  public OperatorVO getCurrentUser(String operatorId) {
    OperatorVO operatorVO = operatorDao.getById(operatorId);
    if (operatorVO != null) {
      clearSensitiveFields(operatorVO);
    }
    return operatorVO;
  }

  /**
   * Clear sensitive and internal fields from OperatorVO
   *
   * <p>This method implements the principle of least privilege by removing: 1. Security-sensitive
   * fields (passwords, verification codes) 2. Audit and version control fields (created/updated
   * by/at)
   *
   * <p>This ensures consistent data sanitization across login and getCurrentUser endpoints.
   *
   * @param operatorVO the operator VO to sanitize
   */
  private void clearSensitiveFields(OperatorVO operatorVO) {
    if (operatorVO == null) {
      return;
    }

    // Security-sensitive fields
    operatorVO.setPassword(null);
    operatorVO.setEmailVerificationCode(null);
    operatorVO.setEmailVerificationCodeSendTime(null);
    operatorVO.setEmailVerificationCodeVerifiedTime(null);
    operatorVO.setMobileVerificationCode(null);
    operatorVO.setMobileVerificationCodeSendTime(null);
    operatorVO.setMobileVerificationCodeVerifiedTime(null);

    // Audit and version control fields (from BaseDomain)
    operatorVO.setCreatedBy(null);
    operatorVO.setUpdatedBy(null);
    operatorVO.setCreatedAt(null);
    operatorVO.setUpdatedAt(null);
    operatorVO.setDeletedAt(null);
    operatorVO.setDataDate(null);
    operatorVO.setRequestId(null);
    operatorVO.setIsDeleted(0); // Keep 0 as default but don't expose actual value
  }
}
