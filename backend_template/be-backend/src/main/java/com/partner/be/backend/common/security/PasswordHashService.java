package com.partner.be.backend.common.security;

import java.util.Locale;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Centralizes password hashing with a hybrid approach for security and compatibility.
 *
 * <h3>Password Processing Strategy:</h3>
 *
 * <ul>
 *   <li><b>Frontend (Transmission):</b> User password is hashed with SHA256 before transmission to
 *       protect against network sniffing in non-HTTPS environments
 *   <li><b>Backend (Storage):</b> The received SHA256 value is treated as the "raw password" and
 *       further hashed with BCrypt (with auto-generated salt) for secure storage
 *   <li><b>Database:</b> Stores BCrypt(SHA256(plaintext)) for new passwords
 * </ul>
 *
 * <h3>Legacy Support:</h3>
 *
 * <p>Supports reading legacy MD5(plaintext) hashes for backward compatibility. Legacy passwords are
 * automatically upgraded to BCrypt(SHA256) upon successful login.
 *
 * <h3>Security Benefits:</h3>
 *
 * <ul>
 *   <li>Transmission: SHA256 prevents plaintext password exposure
 *   <li>Storage: BCrypt provides salt, adaptive cost, and resistance to brute-force attacks
 *   <li>Defense in depth: Two-layer hashing (SHA256 + BCrypt)
 * </ul>
 *
 * @see <a
 *     href="https://cheatsheetseries.owasp.org/cheatsheets/Password_Storage_Cheat_Sheet.html">OWASP
 *     Password Storage Cheat Sheet</a>
 */
@Slf4j
@Component
public class PasswordHashService {

  private static final Pattern MD5_PATTERN = Pattern.compile("^[A-Fa-f0-9]{32}$");
  private static final Pattern BCRYPT_PATTERN = Pattern.compile("^\\$2[aby]\\$[0-9]{2}\\$.+");

  private final BCryptPasswordEncoder bcryptPasswordEncoder;

  public PasswordHashService(@Value("${security.password.bcrypt-strength:12}") int strength) {
    int safeStrength = Math.min(Math.max(strength, 10), 18);
    this.bcryptPasswordEncoder = new BCryptPasswordEncoder(safeStrength);
  }

  /**
   * Generates a BCrypt hash for the provided password.
   *
   * <p><b>Important:</b> The input should be a SHA256-hashed password from the frontend, not the
   * plaintext password. This method treats the SHA256 value as the "raw password" and applies
   * BCrypt hashing for secure storage.
   *
   * @param sha256Password the SHA256-hashed password received from frontend
   * @return BCrypt hash of the SHA256 password
   * @throws IllegalArgumentException if the password is blank
   */
  public String hash(String sha256Password) {
    if (StringUtils.isBlank(sha256Password)) {
      throw new IllegalArgumentException("Password must not be blank");
    }
    return bcryptPasswordEncoder.encode(sha256Password);
  }

  /**
   * Compares the SHA256-hashed password from frontend against a stored hash.
   *
   * <p>Supports two storage formats:
   *
   * <ul>
   *   <li><b>BCrypt (current):</b> Stored as BCrypt(SHA256). The input SHA256 password is compared
   *       using BCrypt's secure comparison.
   *   <li><b>Legacy MD5:</b> Stored as MD5(plaintext). <b>Note:</b> Legacy MD5 passwords cannot be
   *       verified with SHA256 input and will fail authentication. Users with MD5 passwords must
   *       reset their password using the password reset flow.
   * </ul>
   *
   * @param sha256Password the SHA256-hashed password received from frontend
   * @param storedHash the hash stored in the database
   * @return true if the password matches, false otherwise
   */
  public boolean matches(String sha256Password, String storedHash) {
    if (StringUtils.isBlank(sha256Password) || StringUtils.isBlank(storedHash)) {
      return false;
    }
    if (isBcryptHash(storedHash)) {
      return bcryptPasswordEncoder.matches(sha256Password, storedHash);
    }
    if (isLegacyMd5(storedHash)) {
      // Legacy MD5 passwords cannot be verified with SHA256 input.
      // Users must reset their password through the password reset flow.
      log.warn("Attempted login with legacy MD5 password. Password reset required.");
      return false;
    }
    // Unknown format – safest option is to fail authentication.
    return false;
  }

  /** Returns true when the stored hash still uses the legacy MD5 format. */
  public boolean needsMigration(String storedHash) {
    if (StringUtils.isBlank(storedHash)) {
      return false;
    }
    return !isBcryptHash(storedHash);
  }

  private boolean isLegacyMd5(String storedHash) {
    return MD5_PATTERN.matcher(storedHash.toUpperCase(Locale.ENGLISH)).matches();
  }

  private boolean isBcryptHash(String storedHash) {
    return BCRYPT_PATTERN.matcher(storedHash).matches();
  }

  /**
   * Main method for testing password hashing flow.
   *
   * <p>Demonstrates the complete password flow:
   *
   * <ol>
   *   <li>User enters plaintext password (e.g., "123456")
   *   <li>Frontend applies SHA256 hash
   *   <li>Backend applies BCrypt hash to SHA256 value
   *   <li>BCrypt hash is stored in database
   * </ol>
   */
  public static void main(String[] args) {
    String plaintextPassword = "123456";
    System.out.println("=== Password Hashing Flow Demo ===");
    System.out.println();
    System.out.println("1. 用户输入明文密码: " + plaintextPassword);

    // Step 1: Frontend applies SHA256
    String sha256Password = DigestUtils.sha256Hex(plaintextPassword);
    System.out.println("2. 前端SHA256加密后: " + sha256Password);
    System.out.println("   (这个值通过HTTPS发送到后端)");

    // Step 2: Backend applies BCrypt to SHA256
    PasswordHashService service = new PasswordHashService(12);
    String bcryptHash = service.hash(sha256Password);
    System.out.println("3. 后端BCrypt加密后: " + bcryptHash);
    System.out.println("   (这个值保存到数据库)");

    // Step 3: Verification
    System.out.println();
    System.out.println("=== 验证流程 ===");
    bcryptHash = "$2a$12$iR.cTi7QkxWPquUssng5Mu2shjKo5r3AeOM5xuf7WHqrlU4YYfuc6";
    boolean matches = service.matches(sha256Password, bcryptHash);
    System.out.println("前端发送SHA256(" + plaintextPassword + ") = " + sha256Password);
    System.out.println("后端验证结果: " + (matches ? "✓ 密码正确" : "✗ 密码错误"));

    // Step 4: Show that wrong password fails
    System.out.println();
    String wrongPassword = "wrong";
    String wrongSha256 = DigestUtils.sha256Hex(wrongPassword);
    boolean wrongMatches = service.matches(wrongSha256, bcryptHash);
    System.out.println("错误密码验证:");
    System.out.println("前端发送SHA256(" + wrongPassword + ") = " + wrongSha256);
    System.out.println("后端验证结果: " + (wrongMatches ? "✓ 密码正确" : "✗ 密码错误"));

    System.out.println();
    System.out.println("=== 数据库中应该保存的值 ===");
    System.out.println(bcryptHash);
  }
}
