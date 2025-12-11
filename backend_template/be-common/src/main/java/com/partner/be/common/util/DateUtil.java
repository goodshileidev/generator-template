package com.partner.be.common.util;

import java.util.Calendar;
import java.util.Date;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

/**
 * Date Utility Class for Batch Task Processing
 *
 * <p>This utility class provides date manipulation and formatting functionality
 * specifically designed for batch task processing requirements.</p>
 *
 * <p>Features:
 * <ul>
 *   <li>Data date calculation for batch processing intervals</li>
 *   <li>Standard date formatting to ISO-like format</li>
 *   <li>Time alignment to half-hour intervals for consistent data processing</li>
 * </ul>
 * </p>
 *
 * @author System
 * @version 1.0
 */
public class DateUtil {

  /**
   * Calculates the appropriate data date for batch processing
   *
   * <p>This method adjusts the input date to align with half-hour intervals
   * commonly used in batch data processing. It ensures that the start time
   * falls on either the hour or half-hour mark, then subtracts 30 minutes
   * to get the appropriate processing interval.</p>
   *
   * <p>Algorithm:
   * <ul>
   *   <li>If minutes < 30: Set to previous hour (00 minutes)</li>
   *   <li>If minutes >= 30: Set to current hour (30 minutes)</li>
   *   <li>Always subtract 30 minutes to get the processing interval</li>
   * </ul>
   * </p>
   *
   * @param date the input date to process
   * @return adjusted date aligned to half-hour intervals minus 30 minutes
   */
  public static Date getDataDate(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    // Ensure start time aligns to hour or half-hour boundaries
    int minutes = calendar.get(Calendar.MINUTE);
    if (minutes < 30) {
      // Set to previous hour (00 minutes)
      calendar.set(Calendar.MINUTE, 0);
      calendar.set(Calendar.SECOND, 0);
      calendar.set(Calendar.MILLISECOND, 0);
    } else {
      // Set to current hour (30 minutes)
      calendar.set(Calendar.MINUTE, 30);
      calendar.set(Calendar.SECOND, 0);
      calendar.set(Calendar.MILLISECOND, 0);
    }

    // Subtract 30 minutes to get the processing interval
    return DateUtils.addMinutes(calendar.getTime(), -30);
  }

  /**
   * Formats a date to standard string representation
   *
   * <p>This method converts a Date object to a formatted string using
   * the standard "yyyy-MM-dd HH:mm:ss" format, which is commonly used
   * for database storage and API communication.</p>
   *
   * @param date the date to format
   * @return formatted date string in "yyyy-MM-dd HH:mm:ss" format
   */
  public static String format(Date date) {
    String format = "yyyy-MM-dd HH:mm:ss";
    return DateFormatUtils.format(date, format);
  }
}
