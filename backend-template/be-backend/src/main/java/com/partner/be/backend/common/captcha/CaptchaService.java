package com.partner.be.backend.common.captcha;

import com.partner.be.common.filter.UserThreadHolder;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.Base64;
import java.util.Random;
import java.util.UUID;
import javax.imageio.ImageIO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Generates captcha images and coordinates persistence of the expected values.
 */
@Component
@Slf4j
public class CaptchaService {

  private static final int WIDTH = 120;
  private static final int HEIGHT = 40;
  private static final int CODE_LENGTH = 4;
  private static final long CAPTCHA_TTL_SECONDS = 300;
  private static final String IMAGE_FORMAT = "png";
  private static final char[] CHARS =
      "ABCDEFGHJKLMNPQRSTUVWXYZ23456789".toCharArray();

  private final Random random = new Random();

  public CaptchaResponse createCaptcha() {
    String code = randomCode();
    BufferedImage image = renderImage(code);
    String base64 = encode(image);
    String captchaId = UUID.randomUUID().toString();
    UserThreadHolder.storeCaptchaValue(captchaId, code);
    long expireAt = Instant.now().plusSeconds(CAPTCHA_TTL_SECONDS).toEpochMilli();
    return new CaptchaResponse(captchaId, base64, expireAt);
  }

  public boolean validate(String captchaId, String captchaValue) {
    return UserThreadHolder.validateCaptchaValue(
        captchaId, captchaValue, CAPTCHA_TTL_SECONDS);
  }

  private String encode(BufferedImage image) {
    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
      ImageIO.write(image, IMAGE_FORMAT, outputStream);
      return Base64.getEncoder().encodeToString(outputStream.toByteArray());
    } catch (IOException e) {
      log.error("Failed to encode captcha image", e);
      throw new IllegalStateException("Failed to generate captcha", e);
    }
  }

  private BufferedImage renderImage(String code) {
    BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    Graphics2D g2d = image.createGraphics();
    try {
      g2d.setColor(Color.WHITE);
      g2d.fillRect(0, 0, WIDTH, HEIGHT);
      g2d.setRenderingHint(
          RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
      drawNoise(g2d);
      drawCode(g2d, code);
    } finally {
      g2d.dispose();
    }
    return image;
  }

  private void drawNoise(Graphics2D g2d) {
    g2d.setStroke(new BasicStroke(1.2f));
    for (int i = 0; i < 3; i++) {
      g2d.setColor(randomColor(150, 255));
      int x1 = random.nextInt(WIDTH);
      int y1 = random.nextInt(HEIGHT);
      int x2 = random.nextInt(WIDTH);
      int y2 = random.nextInt(HEIGHT);
      g2d.drawLine(x1, y1, x2, y2);
    }
  }

  private void drawCode(Graphics2D g2d, String code) {
    g2d.setFont(new Font("Arial", Font.BOLD, HEIGHT - 8));
    for (int i = 0; i < code.length(); i++) {
      g2d.setColor(randomColor(20, 150));
      int x = 10 + i * (WIDTH - 20) / CODE_LENGTH;
      int y = HEIGHT - 8;
      g2d.drawString(String.valueOf(code.charAt(i)), x, y);
    }
  }

  private Color randomColor(int min, int max) {
    int bound = max - min;
    int r = min + random.nextInt(bound);
    int g = min + random.nextInt(bound);
    int b = min + random.nextInt(bound);
    return new Color(r, g, b);
  }

  private String randomCode() {
    StringBuilder builder = new StringBuilder(CODE_LENGTH);
    for (int i = 0; i < CODE_LENGTH; i++) {
      builder.append(CHARS[random.nextInt(CHARS.length)]);
    }
    return builder.toString();
  }
}
