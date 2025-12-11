package com.partner.be.backend.common.service;

import com.partner.be.backend.common.po.LoginPO;
import com.partner.be.common.system.vo.OperatorVO;

public interface LoginService {
  /**
   * Lock Data
   *
   * @param loginPO
   * @return
   */
  LoginServiceResult login(LoginPO loginPO);

  /**
   * Lock Data
   *
   * @param loginPO
   * @return
   */
  LoginServiceResult logout(LoginPO loginPO);

  /**
   * Change User Password
   *
   * @param updatePO
   * @return
   */
  LoginServiceResult updatePassword(LoginPO updatePO);

  //
  //  /**
  //   * Send Reset Password Email
  //   *
  //   * @param loginPO
  //   * @return
  //   */
  //  LoginServiceResult sendResetPasswordMail(LoginPO loginPO);

  /**
   * Send Email Verification Code
   *
   * @param loginPO
   * @return
   */
  LoginServiceResult sendEmailVerifyCode(LoginPO loginPO);

  /**
   * Reset Password
   *
   * @param loginPO
   * @return
   */
  LoginServiceResult resetPassword(LoginPO loginPO);

  /**
   * Get Current User Information
   *
   * Retrieves user information with sensitive fields removed.
   * Returns only the minimum necessary fields following the principle of least privilege.
   *
   * @param operatorId the operator ID
   * @return operator information with sensitive fields cleared
   */
  OperatorVO getCurrentUser(String operatorId);
}
