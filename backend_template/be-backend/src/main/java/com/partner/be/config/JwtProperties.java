package com.partner.be.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration properties for JWT tokens.
 */
@Configuration
@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtProperties {

  /** Shared secret used to sign tokens. */
  private String secret = "change-me";

  /** Expiration time in seconds. */
  private long expirationSeconds = 24 * 60 * 60;

  /** HTTP header containing the token. */
  private String header = "Authorization";

  /** Prefix (e.g. Bearer). */
  private String prefix = "Bearer";

  /** Token issuer name. */
  private String issuer = "be-app-backend";
}
