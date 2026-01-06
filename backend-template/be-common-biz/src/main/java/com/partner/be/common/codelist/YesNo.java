package com.partner.be.common.codelist;

import com.partner.be.common.util.MessageUtils;

/** Yes/No status */
public enum YesNo {
  yes("yes"),
  no("no"),
  NotMatch("");

  private String code;

  YesNo(String code) {
    this.code = code;
  }

  public String getCode() {
    return code;
  }

  public String getTitle(String language) {
    return MessageUtils.getMessage("codelist.YesNo." + code, language);
  }

  public static YesNo getByCode(String codeValue) {
    for (YesNo code : YesNo.values()) {
      if (code.getCode().equals(codeValue)) {
        return code;
      }
    }
    return NotMatch;
  }
}
