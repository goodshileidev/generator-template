package com.partner.be.notification.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;

/**
 * Email Utility - Helper class for sending emails using Apache Commons Email
 */
@Slf4j
public class EmailUtil {

  /**
   * Send an email using Apache Commons Email
   *
   * @param email the Email object to send
   * @throws EmailException if sending fails
   */
  public static void send(Email email) throws EmailException {
    if (email == null) {
      log.warn("Attempted to send null email");
      return;
    }

    log.info("Sending email to: {} with subject: {}", email.getToAddresses(), email.getSubject());
    email.send();
    log.info("Email sent successfully");
  }
}
