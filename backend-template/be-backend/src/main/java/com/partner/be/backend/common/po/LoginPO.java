package com.partner.be.backend.common.po;

import lombok.Data;

@Data
public class LoginPO {
  String username;
  String email;
  String password;
  String otp;
  String operatorId;

  String oldPassword;

  String newPassword;
  String verifyCode;
  String confirmPassword;
  String language;
  String captchaId;
  String captchaCode;
  String bizType;
}
