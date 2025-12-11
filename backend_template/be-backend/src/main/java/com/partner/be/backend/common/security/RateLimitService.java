package com.partner.be.backend.common.security;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Rate limiting service to prevent abuse of sensitive operations.
 *
 * <p>This service implements sliding window rate limiting to restrict the frequency
 * of operations like login attempts, password resets, and email verification code requests.
 * It uses in-memory storage with automatic expiration to track attempts per identifier.
 *
 * <p><b>Security Features:</b>
 * <ul>
 *   <li>Prevents brute force attacks on authentication endpoints</li>
 *   <li>Limits email bombing and verification code enumeration</li>
 *   <li>Uses sliding window algorithm for accurate rate limiting</li>
 *   <li>Automatic cleanup of expired entries</li>
 * </ul>
 *
 * <p><b>REQ-SEC-RATE-LIMIT:</b> Rate limiting for security-sensitive operations
 *
 * @author System
 * @version 1.0
 */
@Service
@Slf4j
public class RateLimitService {

  /**
   * Cache for tracking login attempts.
   * Key: username/email, Value: attempt counter with timestamp
   */
  private final Cache<String, AttemptCounter> loginAttempts =
      CacheBuilder.newBuilder()
          .expireAfterWrite(15, TimeUnit.MINUTES)
          .maximumSize(10000)
          .build();

  /**
   * Cache for tracking email verification code requests.
   * Key: email address, Value: attempt counter with timestamp
   */
  private final Cache<String, AttemptCounter> emailVerificationAttempts =
      CacheBuilder.newBuilder()
          .expireAfterWrite(60, TimeUnit.MINUTES)
          .maximumSize(10000)
          .build();

  /**
   * Cache for tracking password reset requests.
   * Key: email address, Value: attempt counter with timestamp
   */
  private final Cache<String, AttemptCounter> passwordResetAttempts =
      CacheBuilder.newBuilder()
          .expireAfterWrite(60, TimeUnit.MINUTES)
          .maximumSize(10000)
          .build();

  // Rate limit configuration constants
  private static final int MAX_LOGIN_ATTEMPTS = 5;
  private static final int MAX_EMAIL_VERIFICATION_ATTEMPTS = 3;
  private static final int MAX_PASSWORD_RESET_ATTEMPTS = 3;

  private static final Duration LOGIN_WINDOW = Duration.ofMinutes(15);
  private static final Duration EMAIL_VERIFICATION_WINDOW = Duration.ofMinutes(60);
  private static final Duration PASSWORD_RESET_WINDOW = Duration.ofMinutes(60);

  /**
   * Checks if a login attempt is allowed for the given identifier.
   *
   * @param identifier the username or email attempting to log in
   * @return true if the attempt is allowed, false if rate limit exceeded
   */
  public boolean allowLoginAttempt(String identifier) {
    return checkRateLimit(
        identifier,
        loginAttempts,
        MAX_LOGIN_ATTEMPTS,
        LOGIN_WINDOW,
        "login");
  }

  /**
   * Records a failed login attempt for the given identifier.
   *
   * @param identifier the username or email that failed to log in
   */
  public void recordFailedLogin(String identifier) {
    recordAttempt(identifier, loginAttempts, "login");
  }

  /**
   * Clears login attempts for a successful login.
   *
   * @param identifier the username or email that successfully logged in
   */
  public void clearLoginAttempts(String identifier) {
    loginAttempts.invalidate(identifier);
    if (log.isDebugEnabled()) {
      log.debug("Cleared login attempts for identifier={}", identifier);
    }
  }

  /**
   * Checks if an email verification code request is allowed.
   *
   * @param email the email address requesting verification code
   * @return true if the request is allowed, false if rate limit exceeded
   */
  public boolean allowEmailVerificationRequest(String email) {
    return checkRateLimit(
        email,
        emailVerificationAttempts,
        MAX_EMAIL_VERIFICATION_ATTEMPTS,
        EMAIL_VERIFICATION_WINDOW,
        "email verification");
  }

  /**
   * Records an email verification code request.
   *
   * @param email the email address that requested verification code
   */
  public void recordEmailVerificationRequest(String email) {
    recordAttempt(email, emailVerificationAttempts, "email verification");
  }

  /**
   * Checks if a password reset request is allowed.
   *
   * @param email the email address requesting password reset
   * @return true if the request is allowed, false if rate limit exceeded
   */
  public boolean allowPasswordResetRequest(String email) {
    return checkRateLimit(
        email,
        passwordResetAttempts,
        MAX_PASSWORD_RESET_ATTEMPTS,
        PASSWORD_RESET_WINDOW,
        "password reset");
  }

  /**
   * Records a password reset request.
   *
   * @param email the email address that requested password reset
   */
  public void recordPasswordResetRequest(String email) {
    recordAttempt(email, passwordResetAttempts, "password reset");
  }

  /**
   * Gets the remaining attempts before rate limit is reached.
   *
   * @param identifier the identifier to check
   * @param cache the cache to check
   * @param maxAttempts the maximum allowed attempts
   * @return the number of remaining attempts
   */
  public int getRemainingAttempts(String identifier, Cache<String, AttemptCounter> cache, int maxAttempts) {
    AttemptCounter counter = cache.getIfPresent(identifier);
    if (counter == null) {
      return maxAttempts;
    }
    return Math.max(0, maxAttempts - counter.getCount());
  }

  /**
   * Core rate limiting logic using sliding window algorithm.
   */
  private boolean checkRateLimit(
      String identifier,
      Cache<String, AttemptCounter> cache,
      int maxAttempts,
      Duration window,
      String operationType) {

    AttemptCounter counter = cache.getIfPresent(identifier);

    if (counter == null) {
      // First attempt - allow
      return true;
    }

    // Check if window has expired
    Instant now = Instant.now();
    if (Duration.between(counter.getFirstAttemptTime(), now).compareTo(window) > 0) {
      // Window expired - reset and allow
      cache.invalidate(identifier);
      return true;
    }

    // Check if limit exceeded
    if (counter.getCount() >= maxAttempts) {
      Duration remaining = window.minus(Duration.between(counter.getFirstAttemptTime(), now));
      log.warn(
          "Rate limit exceeded for {} operation. identifier={}, attempts={}/{}, retry_after={}",
          operationType,
          identifier,
          counter.getCount(),
          maxAttempts,
          remaining);
      return false;
    }

    return true;
  }

  /**
   * Records an attempt for the given identifier.
   */
  private void recordAttempt(
      String identifier,
      Cache<String, AttemptCounter> cache,
      String operationType) {

    AttemptCounter counter = cache.getIfPresent(identifier);

    if (counter == null) {
      counter = new AttemptCounter(Instant.now(), new AtomicInteger(1));
      cache.put(identifier, counter);
    } else {
      counter.getAttemptCount().incrementAndGet();
    }

    if (log.isDebugEnabled()) {
      log.debug(
          "Recorded {} attempt for identifier={}, total_attempts={}",
          operationType,
          identifier,
          counter.getCount());
    }
  }

  /**
   * Counter for tracking attempts with timestamp.
   */
  @AllArgsConstructor
  @Getter
  private static class AttemptCounter {
    private final Instant firstAttemptTime;
    private final AtomicInteger attemptCount;

    public int getCount() {
      return attemptCount.get();
    }
  }
}
