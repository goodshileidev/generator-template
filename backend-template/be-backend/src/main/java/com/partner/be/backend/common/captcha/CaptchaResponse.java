package com.partner.be.backend.common.captcha;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO representing a generated captcha image payload.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CaptchaResponse {
  private String captchaId;
  private String image;
  private long expireAt;
}
