package com.lsnju.base.util;

import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author lis614
 * @since 2025/7/22 20:33
 * @version V1.0
 */
public class TpStringMaskUtils {

    private final int exposeFront;
    private final int exposeRear;
    private final char mask;
    private final int maskLength;

    public TpStringMaskUtils(int exposeFront, int exposeRear, char mask, int maskLength) {
        this.exposeFront = exposeFront;
        this.exposeRear = exposeRear;
        this.mask = mask;
        this.maskLength = maskLength;
    }

    public String mask(String value) {
        if (value == null) {
            return null;
        }
        int length = value.length();
        if (length <= exposeFront + exposeRear) {
            return value;
        }
        if (maskLength > 0) {
            return StringUtils.substring(value, 0, exposeFront) + StringUtils.repeat(mask, maskLength) + StringUtils.substring(value, -exposeRear);
        }
        final char[] buffer = value.toCharArray();
        for (int i = exposeFront; i < length - exposeRear; i++) {
            buffer[i] = '*';
        }
        return new String(buffer);
    }

    public static final TpStringMaskUtils DEFAULT = new TpStringMaskUtils(2, 2, '*', -1);

    public static String maskDefault(String value) {
        return DEFAULT.mask(value);
    }

}
