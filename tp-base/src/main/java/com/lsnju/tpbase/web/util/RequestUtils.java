package com.lsnju.tpbase.web.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import ch.qos.logback.classic.ClassicConstants;

/**
 *
 * @author ls
 * @since 2023-07-25 22:46:19
 * @version V1.0
 */
public class RequestUtils {

    public static String getRequestIp() {
        return StringUtils.trimToEmpty(StringUtils.substringBefore(MDC.get(ClassicConstants.REQUEST_X_FORWARDED_FOR), ","));
    }

}
