package com.partner.be.backend.common.security;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * Simple wrapper that allows adding synthetic headers to the current request.
 */
public class MutableHttpServletRequest extends HttpServletRequestWrapper {

  private final Map<String, String> customHeaders = new HashMap<>();

  public MutableHttpServletRequest(HttpServletRequest request) {
    super(request);
  }

  public void putHeader(String name, String value) {
    if (value == null) {
      return;
    }
    customHeaders.put(name, value);
  }

  @Override
  public String getHeader(String name) {
    String headerValue = customHeaders.get(name);
    if (headerValue != null) {
      return headerValue;
    }
    return super.getHeader(name);
  }

  @Override
  public Enumeration<String> getHeaderNames() {
    Enumeration<String> headerNames = super.getHeaderNames();
    Map<String, String> combined = new HashMap<>();
    if (headerNames != null) {
      while (headerNames.hasMoreElements()) {
        String name = headerNames.nextElement();
        combined.put(name, super.getHeader(name));
      }
    }
    combined.putAll(customHeaders);
    return Collections.enumeration(combined.keySet());
  }

  @Override
  public Enumeration<String> getHeaders(String name) {
    if (customHeaders.containsKey(name)) {
      return Collections.enumeration(Collections.singletonList(customHeaders.get(name)));
    }
    return super.getHeaders(name);
  }
}
