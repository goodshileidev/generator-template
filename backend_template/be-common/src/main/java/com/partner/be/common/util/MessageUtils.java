package com.partner.be.common.util;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

@Component
public class MessageUtils {

  static ResourceBundleMessageSource messageSource;

  static Map<String, Locale> locales = new HashMap<>();

  static {
    locales.put("en-US", Locale.ENGLISH);
    locales.put("en", Locale.ENGLISH);
    locales.put("ja-JP", Locale.JAPANESE);
    locales.put("ja", Locale.JAPANESE);
    locales.put("zh-CN", Locale.CHINESE);
    locales.put("zh", Locale.CHINESE);
  }

  @Autowired
  public void setMessageSource(ResourceBundleMessageSource resourceBundleMessageSource) {
    messageSource = resourceBundleMessageSource;
  }

  public static String getMessage(String code, String language, Object... args) {
    Locale locale = locales.get(language);
    if (locale == null && language != null) {
      locale = locales.get(language.toLowerCase(Locale.ROOT));
    }
    if (locale == null) {
      locale = Locale.ENGLISH;
    }
    return messageSource.getMessage(code, args, locale);
  }
}
