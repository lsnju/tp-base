package com.lsnju.base.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author ls
 * @since 2023-07-20 07:26:12
 * @version V1.0
 */
@Slf4j
public class TpDateFormatUtils {

    public static final String longFormat = "yyyyMMddHHmmss";
    public static final String dateFormat = "yyyyMMdd";
    public static final String timeFormat = "HHmmss";

    public static final String newFormat = "yyyy-MM-dd HH:mm:ss";
    public static final String webFormat = "yyyy-MM-dd";
    public static final String alipayFormat = "yyyy/MM/dd";

    /**
     * @param pattern
     * @return DateFormat
     */
    public static DateFormat newDateFormat(String pattern) {
        DateFormat df = new SimpleDateFormat(pattern);
        df.setLenient(false);
        return df;
    }

    /**
     * @param date
     * @param format
     * @return DateFormat
     */
    public static String format(Date date, String format) {
        return format(date, new SimpleDateFormat(format));
    }

    /**
     * @param date
     * @param format
     * @return DateFormat
     */
    public static String format(Date date, DateFormat format) {
        if (date == null || format == null) {
            return null;
        }
        return format.format(date);
    }

    /**
     * @param dateStr
     * @param format
     * @return Date
     */
    public static Date parse(String dateStr, String format) {
        if (StringUtils.isAnyBlank(dateStr, format)) {
            return null;
        }
        DateFormat dateFormat = new SimpleDateFormat(format);
        try {
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    // ========================================================
    // ========================================================
    // ========================================================

    /**
     * yyyyMMddHHmmss
     *
     * @param longDateStr
     * @return Date
     */
    public static Date parseDateLongFormat(String longDateStr) {
        return parse(longDateStr, longFormat);
    }

    /**
     * yyyyMMddHHmmss
     *
     * @param date
     * @return longFormat
     */
    public static String getLongDateString(Date date) {
        return format(date, longFormat);
    }

    // ========================================================

    /**
     * yyyy-MM-dd HH:mm:ss
     *
     * @param newDateStr
     * @return Date
     */
    public static Date parseDateNewFormat(String newDateStr) {
        return parse(newDateStr, newFormat);
    }

    /**
     * yyyy-MM-dd HH:mm:ss
     *
     * @param date
     * @return newFormat
     */
    public static String getNewFormatDateString(Date date) {
        return format(date, newFormat);
    }

    // ========================================================

    /**
     * yyyyMMdd
     *
     * @param dateStr
     * @return Date
     */
    public static Date parseDateFormat(String dateStr) {
        return parse(dateStr, dateFormat);
    }

    /**
     * yyyyMMdd
     *
     * @return 当天的时间格式化为"yyyyMMdd"
     */
    public static String getDateString(Date date) {
        return format(date, dateFormat);
    }

    // ========================================================

    /**
     * HHmmss
     *
     * @param dateStr
     * @return Date
     */
    public static Date parseDateTimeFormat(String dateStr) {
        return parse(dateStr, timeFormat);
    }

    /**
     * HHmmss
     *
     * @return 当天的时间格式化为"yyyyMMdd"
     */
    public static String getDateTimeString(Date date) {
        return format(date, timeFormat);
    }

    // ========================================================

    /**
     * yyyy-MM-dd
     *
     * @param webDateStr
     * @return Date
     */
    public static Date parseDateWebString(String webDateStr) {
        return parse(webDateStr, webFormat);
    }

    /**
     * yyyy-MM-dd
     *
     * @param date
     * @return webFormat
     */
    public static String getWebDateString(Date date) {
        return format(date, webFormat);
    }

    // ========================================================

    /**
     * yyyy/MM/dd
     *
     * @param webDateStr
     * @return Date
     */
    public static Date parseDateAlipayString(String webDateStr) {
        return parse(webDateStr, alipayFormat);
    }

    /**
     * yyyy/MM/dd
     *
     * @param date
     * @return yyyy/MM/dd
     */
    public static String getAlipayDateString(Date date) {
        return format(date, alipayFormat);
    }

    // ========================================================
}
