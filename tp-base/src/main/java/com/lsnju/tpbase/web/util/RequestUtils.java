package com.lsnju.tpbase.web.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import com.lsnju.tpbase.config.LogMdcConstants;

import jakarta.servlet.http.HttpServletRequest;

/**
 *
 * @author ls
 * @since 2023-07-25 22:46:19
 * @version V1.0
 */
public class RequestUtils {

    private static final String SEPARATOR = ",";
    private static final String UNKNOWN = "unknown";
    private static final String[] HEADERS_TO_TRY = {
        "X-Forwarded-For",
        "X-Real-IP",
        "Proxy-Client-IP",
        "WL-Proxy-Client-IP",
        "HTTP_X_FORWARDED_FOR",
        "HTTP_X_FORWARDED",
        "HTTP_X_CLUSTER_CLIENT_IP",
        "HTTP_CLIENT_IP",
        "HTTP_FORWARDED_FOR",
        "HTTP_FORWARDED",
        "HTTP_VIA",
        "REMOTE_ADDR"};

    public static String getRequestIp() {
        return MDC.get(LogMdcConstants.REQUEST_REMOTE_IP_MDC_KEY);
    }

    public static String getRequestIp(HttpServletRequest request) {
        final String ipInMDC = getRequestIp();
        if (StringUtils.isNotBlank(ipInMDC)) {
            return ipInMDC;
        }
        final String ip = tryGetIpFromRequest(request);
        if (StringUtils.isNotBlank(ip)) {
            return StringUtils.trimToEmpty(StringUtils.substringBefore(ip, SEPARATOR));
        }
        return UNKNOWN;
    }

    private static String tryGetIpFromRequest(HttpServletRequest request) {
        for (String header : HEADERS_TO_TRY) {
            String ip = request.getHeader(header);
            if (StringUtils.isNotBlank(ip) && !StringUtils.equalsIgnoreCase(UNKNOWN, ip)) {
                return ip;
            }
        }
        return request.getRemoteAddr();
    }

}
