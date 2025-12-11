package com.partner.be.common.codelist;

import com.partner.be.common.util.MessageUtils;

/**
 * User Role Enumeration
 *
 * <p>Defines the different user roles and access levels in the system.</p>
 *
 * @author System
 */
public enum UserRole {
  /** Normal user with basic viewing and monitoring permissions */
  normal("normal"),

  /** Maintenance user with device management and troubleshooting permissions */
  maintainer("maintainer"),

  /** Administrator with full system access and configuration permissions */
  admin("admin"),

  /** Default value when no matching user role code is found */
  NotMatch("");

  /** The user role code string representation */
  private String code;

  /**
   * Constructs a UserRole enum with the specified code
   *
   * @param code the user role code string
   */
  UserRole(String code) {
    this.code = code;
  }

  /**
   * Gets the user role code value
   *
   * @return the user role code string
   */
  public String getCode() {
    return code;
  }

  /**
   * Gets the localized title for this user role
   *
   * @param language the language code for localization (e.g., "en", "zh")
   * @return the localized user role title
   */
  public String getTitle(String language) {
    return MessageUtils.getMessage("codelist.UserRole." + code, language);
  }

  /**
   * Finds the UserRole enum by its code value
   *
   * @param codeValue the user role code to search for
   * @return the matching UserRole enum, or NotMatch if no match found
   */
  public static UserRole getByCode(String codeValue) {
    for (UserRole code : UserRole.values()) {
      if (code.getCode().equals(codeValue)) {
        return code;
      }
    }
    return NotMatch;
  }
}
