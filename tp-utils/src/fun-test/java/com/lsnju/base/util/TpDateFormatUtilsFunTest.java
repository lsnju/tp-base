package com.lsnju.base.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author lis614
 * @since 2024/2/29 08:58
 * @version V1.0
 */
@Slf4j
public class TpDateFormatUtilsFunTest {

    @Test
    void test_001() throws ParseException {
        // 2024-02-29T00:45:45.938Z
        Date now = new Date();
        log.info("{}", DateFormatUtils.ISO_8601_EXTENDED_DATETIME_FORMAT.format(now));
        log.info("{}", DateFormatUtils.ISO_8601_EXTENDED_DATETIME_TIME_ZONE_FORMAT.format(now));
        log.info("{}", DateFormatUtils.ISO_8601_EXTENDED_DATE_FORMAT.format(now));
        log.info("{}", DateFormatUtils.ISO_8601_EXTENDED_TIME_FORMAT.format(now));
        log.info("{}", DateFormatUtils.ISO_8601_EXTENDED_TIME_TIME_ZONE_FORMAT.format(now));
        log.info("{}", DateFormatUtils.SMTP_DATETIME_FORMAT.format(now));

        log.info("----");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        log.info("{}", simpleDateFormat.format(new Date()));
        log.info("{}", simpleDateFormat.parse("2024-02-29T00:45:45.938Z"));
        log.info("{}", now);
    }

    @Test
    void test_java8() {
        // local
        log.info("{}", DateTimeFormatter.ISO_LOCAL_DATE.format(LocalDateTime.now()));
        log.info("{}", DateTimeFormatter.ISO_LOCAL_TIME.format(LocalDateTime.now()));
        log.info("{}", DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now()));
        log.info("{}", DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now()));
        log.info("{}", DateTimeFormatter.ISO_ORDINAL_DATE.format(LocalDateTime.now()));
        log.info("{}", DateTimeFormatter.ISO_WEEK_DATE.format(LocalDateTime.now()));


        log.info("---------------");

        // zoned
        log.info("{}", DateTimeFormatter.ISO_DATE.format(ZonedDateTime.now()));
        log.info("{}", DateTimeFormatter.ISO_TIME.format(ZonedDateTime.now()));
        log.info("{}", DateTimeFormatter.ISO_OFFSET_DATE.format(ZonedDateTime.now()));
        log.info("{}", DateTimeFormatter.ISO_OFFSET_TIME.format(ZonedDateTime.now()));

        log.info("{}", DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(ZonedDateTime.now()));
        log.info("{}", DateTimeFormatter.ISO_ZONED_DATE_TIME.format(ZonedDateTime.now()));

        log.info("{}", DateTimeFormatter.ISO_DATE_TIME.format(ZonedDateTime.now()));

        log.info("{}", DateTimeFormatter.ISO_ORDINAL_DATE.format(ZonedDateTime.now()));
        log.info("{}", DateTimeFormatter.ISO_WEEK_DATE.format(ZonedDateTime.now()));

        log.info("{}", DateTimeFormatter.ISO_INSTANT.format(ZonedDateTime.now()));
        log.info("{}", DateTimeFormatter.BASIC_ISO_DATE.format(ZonedDateTime.now()));
        log.info("{}", DateTimeFormatter.RFC_1123_DATE_TIME.format(ZonedDateTime.now()));
    }
}
