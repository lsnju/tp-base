package com.lsnju.base.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

/**
 *
 * @author lisong
 * @since 2022/7/11 10:14
 * @version V1.0
 */
public class TpDateUtils {

    public static final ZoneOffset DEFAULT_OFFSET = ZoneId.systemDefault().getRules().getOffset(Instant.now());

    public static Date dayOfBegin(Date date) {
        return beginOfDay(date);
    }

    public static Date beginOfDay(Date date) {
        return DateUtils.truncate(date, Calendar.DAY_OF_MONTH);
    }

    public static Date beginOfYesterday() {
        return beginOfDay(DateUtils.addDays(new Date(), -1));
    }

    public static Date beginOfTomorrow() {
        return beginOfDay(DateUtils.addDays(new Date(), 1));
    }

    public static ZonedDateTime toZoneDateTime(Date date) {
        return toZoneDateTime(date, ZoneId.systemDefault());
    }

    public static ZonedDateTime toZoneDateTime(Date date, ZoneId zoneId) {
        Objects.requireNonNull(date);
        return ZonedDateTime.ofInstant(date.toInstant(), zoneId != null ? zoneId : ZoneId.systemDefault());
    }

    public static ZonedDateTime parseToZonedDateTime(String date) {
        return parseToZonedDateTime(date, ZoneId.systemDefault());
    }

    public static ZonedDateTime parseToZonedDateTime(String date, ZoneId zoneId) {
        if (StringUtils.endsWithIgnoreCase(date, "Z")) {
            try {
                return ZonedDateTime.ofInstant(Instant.parse(date), zoneId);
            } catch (Exception ignore) {
            }
        }
        try {
            return ZonedDateTime.parse(date, DateTimeFormatter.ISO_ZONED_DATE_TIME);
        } catch (Exception ignore) {
        }
        try {
            return ZonedDateTime.of(LocalDateTime.parse(date, DateTimeFormatter.ISO_LOCAL_DATE_TIME), zoneId);
        } catch (Exception ignore) {
        }
        try {
            return ZonedDateTime.of(LocalDateTime.parse(date, DateTimeFormatter.ISO_LOCAL_DATE), zoneId);
        } catch (Exception ignore) {
        }
        return null;
    }

    public static Date parseToDate(String zonedDateStr) {
        if (StringUtils.endsWithIgnoreCase(zonedDateStr, "Z")) {
            try {
                return Date.from(Instant.parse(zonedDateStr));
            } catch (Exception ignore) {
            }
        }
        try {
            return Date.from(ZonedDateTime.parse(zonedDateStr, DateTimeFormatter.ISO_OFFSET_DATE_TIME).toInstant());
        } catch (Exception ignore) {
        }
        try {
            return Date.from(LocalDateTime.parse(zonedDateStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME).toInstant(DEFAULT_OFFSET));
        } catch (Exception ignore) {
        }
        try {
            return Date.from(LocalDateTime.parse(zonedDateStr, DateTimeFormatter.ISO_LOCAL_DATE).toInstant(DEFAULT_OFFSET));
        } catch (Exception ignore) {
        }
        return TpDateFormatUtils.parseDateNewFormat(zonedDateStr);
    }

}
