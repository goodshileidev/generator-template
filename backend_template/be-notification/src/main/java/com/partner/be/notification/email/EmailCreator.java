package com.partner.be.notification.email;

import com.partner.be.common.system.vo.OperatorVO;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Email Creator - Utility class for creating and sending HTML emails
 *
 * This class uses Apache Commons Email library to send HTML-formatted emails.
 * Email configuration is loaded from application properties.
 */
@Slf4j
@Component
public class EmailCreator {

  @Value("${email.smtp.host:smtp.gmail.com}")
  private String smtpHost;

  @Value("${email.smtp.port:587}")
  private int smtpPort;

  @Value("${email.smtp.username:}")
  private String smtpUsername;

  @Value("${email.smtp.password:}")
  private String smtpPassword;

  @Value("${email.from.address:noreply@partner.com}")
  private String fromAddress;

  @Value("${email.from.name:Partner System}")
  private String fromName;

  @Value("${email.smtp.ssl.enable:false}")
  private boolean sslEnabled;

  @Value("${email.smtp.starttls.enable:true}")
  private boolean startTlsEnabled;

  /**
   * Create an email from a template (simplified version without Velocity)
   *
   * @param templateName template identifier
   * @param context template variables
   * @param recipients list of recipients
   * @param language language preference
   * @return configured Email object
   * @throws EmailException if email creation fails
   */
  public Email create(String templateName, Map<String, Object> context, List<OperatorVO> recipients, String language) throws EmailException {
    HtmlEmail email = new HtmlEmail();
    email.setHostName(smtpHost);
    email.setSmtpPort(smtpPort);
    email.setAuthentication(smtpUsername, smtpPassword);
    email.setSSLOnConnect(sslEnabled);
    email.setStartTLSEnabled(startTlsEnabled);
    email.setFrom(fromAddress, fromName);

    // Add recipients
    if (recipients != null && !recipients.isEmpty()) {
      for (OperatorVO recipient : recipients) {
        if (recipient.getEmail() != null) {
          email.addTo(recipient.getEmail());
        }
      }
    }

    // Simple template handling - in production this should use Velocity or similar
    String subject = "Notification";
    String content = generateSimpleContent(templateName, context);

    if ("account_send_email_verify_code".equals(templateName)) {
      subject = "Email Verification Code";
    } else if ("account_reset_password_email".equals(templateName)) {
      subject = "Password Reset Request";
    }

    email.setSubject(subject);
    email.setHtmlMsg(content);

    return email;
  }

  /**
   * Generate simple HTML content from context
   */
  private String generateSimpleContent(String templateName, Map<String, Object> context) {
    StringBuilder html = new StringBuilder();
    html.append("<html><body>");
    html.append("<h2>Partner System</h2>");

    if (context.containsKey("code")) {
      html.append("<p>Your verification code is: <strong>").append(context.get("code")).append("</strong></p>");
    }
    if (context.containsKey("expireMinutes")) {
      html.append("<p>This code will expire in ").append(context.get("expireMinutes")).append(" minutes.</p>");
    }
    if (context.containsKey("resetLink")) {
      html.append("<p><a href=\"").append(context.get("resetLink")).append("\">Click here to reset your password</a></p>");
    }

    html.append("<p>If you did not request this, please ignore this email.</p>");
    html.append("</body></html>");

    return html.toString();
  }

  /**
   * Create and send an HTML email
   *
   * @param toEmail recipient email address
   * @param subject email subject
   * @param htmlContent HTML content of the email
   * @throws EmailException if email sending fails
   */
  public void sendHtmlEmail(String toEmail, String subject, String htmlContent) throws EmailException {
    HtmlEmail email = new HtmlEmail();
    email.setHostName(smtpHost);
    email.setSmtpPort(smtpPort);
    email.setAuthentication(smtpUsername, smtpPassword);
    email.setSSLOnConnect(sslEnabled);
    email.setStartTLSEnabled(startTlsEnabled);
    email.setFrom(fromAddress, fromName);
    email.setSubject(subject);
    email.setHtmlMsg(htmlContent);
    email.addTo(toEmail);

    log.info("Sending email to: {} with subject: {}", toEmail, subject);
    email.send();
    log.info("Email sent successfully to: {}", toEmail);
  }

  /**
   * Create verification code email content
   *
   * @param code verification code
   * @param validityMinutes validity period in minutes
   * @return HTML email content
   */
  public String createVerificationCodeEmail(String code, long validityMinutes) {
    return String.format(
        "<html>" +
            "<body>" +
            "<h2>Email Verification Code</h2>" +
            "<p>Your verification code is: <strong style='font-size: 24px; color: #007bff;'>%s</strong></p>" +
            "<p>This code will expire in %d minutes.</p>" +
            "<p>If you did not request this code, please ignore this email.</p>" +
            "<br/>" +
            "<p>Best regards,<br/>Partner System</p>" +
            "</body>" +
            "</html>",
        code, validityMinutes);
  }

  /**
   * Create password reset email content
   *
   * @param resetLink password reset link
   * @param validityMinutes validity period in minutes
   * @return HTML email content
   */
  public String createPasswordResetEmail(String resetLink, long validityMinutes) {
    return String.format(
        "<html>" +
            "<body>" +
            "<h2>Password Reset Request</h2>" +
            "<p>You have requested to reset your password.</p>" +
            "<p>Please click the link below to reset your password:</p>" +
            "<p><a href='%s' style='color: #007bff; text-decoration: none;'>Reset Password</a></p>" +
            "<p>This link will expire in %d minutes.</p>" +
            "<p>If you did not request a password reset, please ignore this email.</p>" +
            "<br/>" +
            "<p>Best regards,<br/>Partner System</p>" +
            "</body>" +
            "</html>",
        resetLink, validityMinutes);
  }
}
