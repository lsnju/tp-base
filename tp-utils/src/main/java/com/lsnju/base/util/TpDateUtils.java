package com.lsnju.base.util;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

/**
 *
 * @author lisong
 * @since 2022/7/11 10:14
 * @version V1.0
 */
public class TpDateUtils {

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

}
