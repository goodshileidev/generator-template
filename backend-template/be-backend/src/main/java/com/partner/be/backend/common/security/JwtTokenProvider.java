package com.partner.be.backend.common.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.partner.be.common.system.vo.OperatorVO;
import com.partner.be.config.JwtProperties;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * Helper component that creates and validates JWT tokens.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {

  private static final String CLAIM_USERNAME = "username";
  private static final String CLAIM_DISPLAY_NAME = "displayName";
  private static final String CLAIM_COMPANY_ID = "companyId";
  private static final String CLAIM_POWER_STATION_IDS = "powerStationIds";
  private static final String CLAIM_USER_ROLE = "userRole";

  private final JwtProperties properties;

  /**
   * Generates a signed JWT for the provided user payload.
   */
  public String createToken(OperatorVO operator) {
    if (operator == null || StringUtils.isBlank(operator.getOperatorId())) {
      throw new IllegalArgumentException("Operator cannot be null when issuing JWT");
    }
    Instant issuedAt = Instant.now();
    Instant expiresAt = issuedAt.plusSeconds(Math.max(properties.getExpirationSeconds(), 60));
    try {
      JWTCreator.Builder builder =
          JWT.create()
              .withIssuer(properties.getIssuer())
              .withSubject(operator.getOperatorId())
              .withIssuedAt(Date.from(issuedAt))
              .withExpiresAt(Date.from(expiresAt));
      if (StringUtils.isNotBlank(operator.getUsername())) {
        builder.withClaim(CLAIM_USERNAME, operator.getUsername());
      }
      // REQ-JWT-DISPLAYNAME: Add display name to JWT token (lastName + space + firstName)
      String displayName = buildDisplayName(operator.getLastName(), operator.getFirstName());
      if (StringUtils.isNotBlank(displayName)) {
        builder.withClaim(CLAIM_DISPLAY_NAME, displayName);
      }
      if (StringUtils.isNotBlank(operator.getCompanyId())) {
        builder.withClaim(CLAIM_COMPANY_ID, operator.getCompanyId());
      }
      if (StringUtils.isNotBlank(operator.getPowerStationIds())) {
        builder.withClaim(CLAIM_POWER_STATION_IDS, operator.getPowerStationIds());
      }
      if (StringUtils.isNotBlank(operator.getUserRole())) {
        builder.withClaim(CLAIM_USER_ROLE, operator.getUserRole());
      }
      return builder.sign(algorithm());
    } catch (JWTCreationException exception) {
      throw new IllegalStateException("Failed to create JWT", exception);
    }
  }

  /**
   * Parses and validates the provided token.
   *
   * @param token raw JWT token without prefix
   * @return parsed user details if valid
   */
  public Optional<JwtUserDetails> parseToken(String token) {
    if (StringUtils.isBlank(token)) {
      return Optional.empty();
    }
    try {
      JWTVerifier verifier =
          JWT.require(algorithm())
              .withIssuer(properties.getIssuer())
              .build();
      DecodedJWT decodedJWT = verifier.verify(token);
      return Optional.of(
          JwtUserDetails.builder()
              .operatorId(decodedJWT.getSubject())
              .username(decodedJWT.getClaim(CLAIM_USERNAME).asString())
              .displayName(decodedJWT.getClaim(CLAIM_DISPLAY_NAME).asString())
              .companyId(decodedJWT.getClaim(CLAIM_COMPANY_ID).asString())
              .powerStationIds(decodedJWT.getClaim(CLAIM_POWER_STATION_IDS).asString())
              .userRole(decodedJWT.getClaim(CLAIM_USER_ROLE).asString())
              .issuedAt(decodedJWT.getIssuedAt())
              .expiresAt(decodedJWT.getExpiresAt())
              .build());
    } catch (JWTVerificationException ex) {
      log.warn("JWT verification failed: {}", ex.getMessage());
      return Optional.empty();
    }
  }

  @Getter
  @Builder
  public static class JwtUserDetails {
    private final String operatorId;
    private final String username;
    private final String displayName;
    private final String companyId;
    private final String powerStationIds;
    private final String userRole;
    private final Date issuedAt;
    private final Date expiresAt;
  }

  /**
   * Builds display name from lastName and firstName
   * Format: lastName + space + firstName
   *
   * @param lastName the user's last name
   * @param firstName the user's first name
   * @return display name in the format "lastName firstName", or null if both names are blank
   */
  private String buildDisplayName(String lastName, String firstName) {
    boolean hasLastName = StringUtils.isNotBlank(lastName);
    boolean hasFirstName = StringUtils.isNotBlank(firstName);

    if (hasLastName && hasFirstName) {
      return lastName.trim() + " " + firstName.trim();
    } else if (hasLastName) {
      return lastName.trim();
    } else if (hasFirstName) {
      return firstName.trim();
    }
    return null;
  }

  private Algorithm algorithm() {
    if (StringUtils.isBlank(properties.getSecret())) {
      throw new IllegalStateException("JWT secret must not be empty");
    }
    return Algorithm.HMAC256(properties.getSecret());
  }
}
