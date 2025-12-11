package com.partner.be.backend.common.security;

import com.partner.be.backend.common.security.JwtTokenProvider.JwtUserDetails;
import com.partner.be.common.ApiConstants;
import com.partner.be.common.ApiLoginUser;
import com.partner.be.common.filter.UserThreadHolder;
import com.partner.be.config.JwtProperties;
import com.partner.be.config.WebSecurityPathConfig;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Validates Authorization headers, enforces JWT authentication and populates the user context.
 */
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private static final Set<String> DEFAULT_EXCLUDED_PATHS =
      Collections.unmodifiableSet(
          new HashSet<>(
              Arrays.asList(
                  "/login",
                  "/login/",
                  "/account/resetPassword",
                  "/account/sendEmailVerifyCode",
                  "/account/sendResetPasswordMail",
                  "/captcha")));

  private final WebSecurityPathConfig webSecurityPathConfig;
  private final JwtTokenProvider jwtTokenProvider;
  private final JwtProperties jwtProperties;

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    if (HttpMethod.OPTIONS.matches(request.getMethod())) {
      return true;
    }
    String path = extractPath(request);
    if (DEFAULT_EXCLUDED_PATHS.contains(path)) {
      return true;
    }
    List<String> excluded = webSecurityPathConfig.getAuthenticationExclude();
    if (excluded == null) {
      return false;
    }
    return excluded.stream().filter(StringUtils::isNotBlank).anyMatch(path::matches);
  }

  private String extractPath(HttpServletRequest request) {
    String requestUri = request.getRequestURI();
    String contextPath = request.getContextPath();
    String path = requestUri;
    if (StringUtils.isNotEmpty(contextPath) && requestUri.startsWith(contextPath)) {
      path = requestUri.substring(contextPath.length());
    }
    if (StringUtils.isBlank(path)) {
      path = request.getServletPath();
    }
    return StringUtils.defaultIfBlank(path, "/");
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    if (log.isDebugEnabled()) {
      log.debug(
          "JWT filter invoked for uri={}, servletPath={}",
          request.getRequestURI(),
          request.getServletPath());
    }
    String rawHeader = request.getHeader(jwtProperties.getHeader());
    String token = resolveToken(rawHeader);
    if (StringUtils.isBlank(token)) {
      writeUnauthorizedResponse(response, "Missing Authorization header");
      return;
    }

    Optional<JwtUserDetails> userDetailsOptional = jwtTokenProvider.parseToken(token);
    if (!userDetailsOptional.isPresent()) {
      writeUnauthorizedResponse(response, "Invalid or expired token");
      return;
    }

    JwtUserDetails userDetails = userDetailsOptional.get();
    MutableHttpServletRequest mutableRequest = new MutableHttpServletRequest(request);
    try {
      populateContext(mutableRequest, userDetails);
      filterChain.doFilter(mutableRequest, response);
    } finally {
      clearContext();
    }
  }

  private String resolveToken(String headerValue) {
    if (StringUtils.isBlank(headerValue)) {
      return null;
    }
    String configuredPrefix = StringUtils.defaultString(jwtProperties.getPrefix());
    String prefix = configuredPrefix + " ";
    if (StringUtils.isNotBlank(configuredPrefix)
        && StringUtils.startsWithIgnoreCase(headerValue, prefix)) {
      return headerValue.substring(prefix.length()).trim();
    }
    return headerValue.trim();
  }

  private void populateContext(MutableHttpServletRequest request, JwtUserDetails userDetails) {
    if (StringUtils.isNotBlank(userDetails.getOperatorId())) {
      request.putHeader("loginUserId", userDetails.getOperatorId());
      request.putHeader("userId", userDetails.getOperatorId());
      UserThreadHolder.set(UserThreadHolder.LOGIN_USER_ID, userDetails.getOperatorId());
    }
    if (StringUtils.isNotBlank(userDetails.getUsername())) {
      request.putHeader("loginName", userDetails.getUsername());
    }
    if (StringUtils.isNotBlank(userDetails.getCompanyId())) {
      request.putHeader("loginUserCompanyId", userDetails.getCompanyId());
      UserThreadHolder.set(UserThreadHolder.LOGIN_USER_COMPANY_ID, userDetails.getCompanyId());
    }
    String powerStationIds = userDetails.getPowerStationIds();
    if (StringUtils.isNotBlank(powerStationIds)) {
      request.putHeader("loginUserPowerStationIds", powerStationIds);
      List<String> ids =
          Arrays.stream(powerStationIds.split(","))
              .map(String::trim)
              .filter(StringUtils::isNotEmpty)
              .collect(Collectors.toList());
      UserThreadHolder.set(UserThreadHolder.LOGIN_USER_POWER_STATION_IDS, ids);
    } else {
      UserThreadHolder.set(UserThreadHolder.LOGIN_USER_POWER_STATION_IDS, Collections.emptyList());
    }
    if (StringUtils.isNotBlank(userDetails.getUserRole())) {
      request.putHeader("loginUserRole", userDetails.getUserRole());
      UserThreadHolder.set(UserThreadHolder.LOGIN_USER_ROLE, userDetails.getUserRole());
    }
    ApiLoginUser loginUser = new ApiLoginUser();
    loginUser.setUserId(userDetails.getOperatorId());
    loginUser.setName(userDetails.getUsername());
    loginUser.setMenuRight(new HashSet<>());
    UserThreadHolder.set(ApiConstants.Fields.USER_VALUE_OBJECT_KEY, loginUser);
  }

  private void clearContext() {
    UserThreadHolder.remove(UserThreadHolder.LOGIN_USER_ID);
    UserThreadHolder.remove(UserThreadHolder.LOGIN_USER_COMPANY_ID);
    UserThreadHolder.remove(UserThreadHolder.LOGIN_USER_POWER_STATION_IDS);
    UserThreadHolder.remove(UserThreadHolder.LOGIN_USER_ROLE);
    UserThreadHolder.remove(ApiConstants.Fields.USER_VALUE_OBJECT_KEY);
  }

  private void writeUnauthorizedResponse(HttpServletResponse response, String message)
      throws IOException {
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType("application/json;charset=UTF-8");
    response
        .getWriter()
        .write(String.format("{\"code\":401,\"message\":\"%s\"}", message));
    response.getWriter().flush();
  }
}
