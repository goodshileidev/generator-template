package com.partner.be.backend.common.controller;

import com.partner.be.backend.common.captcha.CaptchaResponse;
import com.partner.be.backend.common.captcha.CaptchaService;
import com.partner.be.common.result.ServiceResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Provides captcha images for the login flow.
 */
@Api("API")
@RestController
@RequestMapping("/captcha")
@RequiredArgsConstructor
public class CaptchaController {

  private final CaptchaService captchaService;

  @GetMapping
  @ApiOperation(value = "Generate image captcha")
  public ServiceResult<CaptchaResponse> generateCaptcha() {
    CaptchaResponse captchaResponse = captchaService.createCaptcha();
    return new ServiceResult<>(captchaResponse);
  }
}
