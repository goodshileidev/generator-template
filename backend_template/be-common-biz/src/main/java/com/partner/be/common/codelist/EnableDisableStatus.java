package com.partner.be.common.codelist;

import com.partner.be.common.util.MessageUtils;

/** Enable/Disable status */
public enum EnableDisableStatus {
  enabled("enabled"),
  disable("disable"),
  NotMatch("");

  private String code;

  EnableDisableStatus(String code) {
    this.code = code;
  }

  public String getCode() {
    return code;
  }

  public String getTitle(String language) {
    return MessageUtils.getMessage("codelist.EnableDisableStatus." + code, language);
  }

  public static EnableDisableStatus getByCode(String codeValue) {
    for (EnableDisableStatus code : EnableDisableStatus.values()) {
      if (code.getCode().equals(codeValue)) {
        return code;
      }
    }
    return NotMatch;
  }
}
