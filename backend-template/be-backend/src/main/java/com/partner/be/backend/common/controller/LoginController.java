package com.partner.be.backend.common.controller;

import com.partner.be.backend.common.captcha.CaptchaService;
import com.partner.be.backend.common.po.LoginPO;
import com.partner.be.backend.common.service.LoginService;
import com.partner.be.backend.common.service.LoginServiceResult;
import com.partner.be.backend.system.service.OperatorServiceResult;
import com.partner.be.backend.system.service.impl.OperatorServiceImpl;
import com.partner.be.common.AbstractApiController;
import com.partner.be.common.codelist.YesNo;
import com.partner.be.common.filter.UserThreadHolder;
import com.partner.be.common.system.po.OperatorPO;
import com.partner.be.common.system.vo.OperatorVO;
import com.partner.be.config.JwtProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.apache.commons.lang3.StringUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import javax.servlet.http.HttpServletResponse;

/**
 * Login Controller
 *
 * REST controller for handling user authentication and account management operations.
 * Provides endpoints for user login, logout, current user information retrieval,
 * and account management functions including password updates and email verification.
 *
 * @author ${author}
 * @email ${email}
 * @date ${datetime}
 */
@Slf4j
@Api("API")
@RestController
public class LoginController extends AbstractApiController {

  private static final long EMAIL_VERIFY_CODE_EXPIRATION_MINUTES = 24 * 60L;

  @Autowired private LoginService loginService;
  @Autowired private CaptchaService captchaService;
  @Autowired private OperatorServiceImpl operatorService;
  @Autowired private JwtProperties jwtProperties;

  /**
   * User login
   *
   * Authenticates user credentials and creates a user session.
   * Validates username and password against the system.
   *
   * @param lockPO login parameters object containing username and password
   * @return service result containing authentication status and user information
   */
  @RequestMapping(path = "/login", method = RequestMethod.POST)
  @ApiOperation(value = "API Operation")
  @ApiImplicitParam(name = "loginPO", value = "Login parameters", dataType = "LoginPO", type = "body")
  // @RequiresPermissions("${module}::create")
  public LoginServiceResult login(@RequestBody LoginPO lockPO, HttpServletResponse response) {
    if (!captchaService.validate(lockPO.getCaptchaId(), lockPO.getCaptchaCode())) {
      return new LoginServiceResult(LoginServiceResult.VERIFY_CODE_ERROR);
    }
    LoginServiceResult serviceResult = loginService.login(lockPO);
    if (serviceResult != null
        && serviceResult.isSuccess()
        && StringUtils.isNotBlank(serviceResult.getToken())) {
      response.setHeader(
          jwtProperties.getHeader(),
          String.format("%s %s", jwtProperties.getPrefix(), serviceResult.getToken()));
    }
    return serviceResult;
  }

  /**
   * User logout
   *
   * Invalidates the current user session and logs the user out of the system.
   * Clears authentication tokens and session data.
   *
   * @param lockPO logout parameters object
   * @return service result containing logout status
   */
  @RequestMapping(path = "/logout", method = RequestMethod.POST)
  @ApiOperation(value = "API Operation")
  @ApiImplicitParam(name = "lockPO", value = "Logout parameters", dataType = "LoginPO", type = "body")
  // @RequiresPermissions("${module}::create")
  public LoginServiceResult logout(@RequestBody LoginPO lockPO) {
    LoginServiceResult serviceResult = loginService.logout(lockPO);
    return serviceResult;
  }

  /**
   * Get current user information
   *
   * Retrieves the current authenticated user's information from JWT token.
   * The operator ID is automatically extracted from the JWT token by JwtAuthenticationFilter.
   * Returns only the minimum necessary fields, consistent with the login endpoint.
   *
   * @return operator information for the current user with sensitive fields removed
   */
  @RequestMapping(path = "/currentUser", method = RequestMethod.POST)
  @ApiOperation(value = "API Operation")
  // @RequiresPermissions("${module}::create")
  public OperatorVO currentUser() {
    // Get current user ID from JWT token via UserThreadHolder
    String operatorId = (String) UserThreadHolder.get(UserThreadHolder.LOGIN_USER_ID);
    // Use loginService.getCurrentUser to ensure consistent field sanitization
    OperatorVO serviceResult = loginService.getCurrentUser(operatorId);
    return serviceResult;
  }

  /**
   * Update user account information
   *
   * Updates the current user's account information including email and mobile number.
   * Uses the authenticated user's ID from the thread context.
   *
   * @param operatorPO operator parameters object containing updated information
   * @return service result containing the update status
   */
  @RequestMapping(path = "/account/updateInfo", method = RequestMethod.POST)
  @ApiOperation(value = "API Operation")
  @ApiImplicitParam(name = "operatorPO", value = "Operator parameters", dataType = "LoginPO", type = "body")
  // @RequiresPermissions("${module}::create")
  public OperatorServiceResult updateInfo(@RequestBody OperatorPO operatorPO) {
    String operatorId = (String) UserThreadHolder.get(UserThreadHolder.LOGIN_USER_ID);
    OperatorVO currentOperator = operatorService.getById(operatorId);
    if (currentOperator == null) {
      return new OperatorServiceResult<>(OperatorServiceResult.USER_NO_EXIST);
    }

    OperatorPO updatePO = new OperatorPO();
    updatePO.setOperatorId(operatorId);

    String requestedEmail = operatorPO.getEmail();
    boolean isEmailChanged =
        StringUtils.isNotBlank(requestedEmail)
            && !StringUtils.equalsIgnoreCase(requestedEmail, currentOperator.getEmail());

    if (isEmailChanged) {
      if (StringUtils.isBlank(operatorPO.getVerifyCode())) {
        return new OperatorServiceResult<>(LoginServiceResult.VERIFY_CODE_ERROR);
      }
      if (!StringUtils.equalsIgnoreCase(
          operatorPO.getVerifyCode(), currentOperator.getEmailVerificationCode())) {
        return new OperatorServiceResult<>(LoginServiceResult.VERIFY_CODE_ERROR);
      }
      if (isVerifyCodeExpired(currentOperator.getEmailVerificationCodeSendTime())) {
        return new OperatorServiceResult<>(LoginServiceResult.VERIFY_CODE_EXPIRED);
      }
      updatePO.setEmail(requestedEmail);
      updatePO.setEmailVerificationCode("");
      updatePO.setEmailVerificationCodeVerifiedTime(new Date());
      updatePO.setEmailVerificationCodeSendTime(new Date(0));
      updatePO.setAssociatedEmailVerificationStatus(YesNo.yes.getCode());
    }

    if (operatorPO.getMobile() != null) {
      updatePO.setMobile(operatorPO.getMobile());
      updatePO.setAssociatedMobileNumber(operatorPO.getMobile());
    }

    OperatorServiceResult serviceResult = operatorService.update(updatePO);
    return serviceResult;
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
   * Update user password
   *
   * Updates the current user's password.
   * Validates the current password and sets the new password.
   *
   * @param loginPO login parameters object containing current and new password
   * @return service result containing the password update status
   */
  @RequestMapping(path = "/account/updatePassword", method = RequestMethod.POST)
  @ApiOperation(value = "API Operation")
  @ApiImplicitParam(name = "loginPO", value = "Login parameters", dataType = "LoginPO", type = "body")
  // @RequiresPermissions("${module}::create")
  public LoginServiceResult updatePassword(@RequestBody LoginPO loginPO) {
    LoginServiceResult serviceResult = loginService.updatePassword(loginPO);
    return serviceResult;
  }

  /**
   * Reset user password
   *
   * Initiates a password reset process by sending a reset password email.
   * Typically used when users forget their passwords.
   *
   * @param loginPO login parameters object containing user email
   * @return service result containing the password reset status
   */
  @RequestMapping(path = "/account/resetPassword", method = RequestMethod.POST)
  @ApiOperation(value = "API Operation")
  @ApiImplicitParam(name = "loginPO", value = "Login parameters", dataType = "LoginPO", type = "body")
  // @RequiresPermissions("${module}::create")
  public LoginServiceResult resetPassword(@RequestBody LoginPO loginPO) {
    LoginServiceResult serviceResult = loginService.resetPassword(loginPO);
    return serviceResult;
  }

  /**
   * Send email verification code
   *
   * Sends an email verification code to the user's registered email address.
   * Used for email verification during registration or account recovery.
   *
   * @param loginPO login parameters object containing user email
   * @return service result containing the email sending status
   */
  @RequestMapping(path = "/account/sendEmailVerifyCode", method = RequestMethod.POST)
  @ApiOperation(value = "API Operation")
  @ApiImplicitParam(name = "loginPO", value = "Login parameters", dataType = "LoginPO", type = "body")
  // @RequiresPermissions("${module}::create")
  public LoginServiceResult sendEmailVerifyCode(@RequestBody LoginPO loginPO) {
    LoginServiceResult serviceResult = loginService.sendEmailVerifyCode(loginPO);
    return serviceResult;
  }
}
