package com.lsnju.base.util;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.lsnju.base.enums.TpBaseEnum;
import com.lsnju.base.money.Money;

/**
 *
 * @author lis614
 * @since 2023/11/15 09:00
 * @version V1.0
 */
public class TpStringUtils {

    public static <T extends TpBaseEnum> String codeEmpty(T enumType) {
        if (enumType != null) {
            return enumType.getCode();
        }
        return StringUtils.EMPTY;
    }

    public static <T extends TpBaseEnum> String codeNull(T enumType) {
        if (enumType != null) {
            return enumType.getCode();
        }
        return null;
    }

    public static long cent(Money money) {
        if (money == null) {
            return 0L;
        }
        return money.getCent();
    }

    public static String fixLen(String value, int length) {
        return StringUtils.defaultString(StringUtils.left(value, length));
    }

    public static Date defaultDate(Date date) {
        return date != null ? date : new Date();
    }

}
