package com.lsnju.tpbase.web.util;

import java.util.Enumeration;
import java.util.Map;
import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author ls
 * @since 2023-07-25 22:39:07
 * @version V1.0
 */
@Slf4j
public class TpHttpHeaderUtils {

    public static String getUserAgent(Map<String, String> headers) {
        return getHeader(headers, HttpHeaders.USER_AGENT);
    }

    public static String getUserAgent(HttpServletRequest request) {
        return getHeader(request, HttpHeaders.USER_AGENT);
    }

    public static String getUserAgent(HttpHeaders headers) {
        return getHeader(headers, HttpHeaders.USER_AGENT);
    }

    public static String getHeader(Map<String, String> headers, String headerName) {
        final String value = headers.get(headerName);
        if (StringUtils.isNotBlank(value)) {
            return value;
        }
        for (String name : headers.keySet()) {
            if (StringUtils.equalsIgnoreCase(headerName, name)) {
                if (!StringUtils.equals(headerName, name)) {
                    log.info("http-header.key = {}", name);
                }
                return headers.get(name);
            }
        }
        return null;
    }

    public static String getHeader(HttpServletRequest request, String headerName) {
        final String value = request.getHeader(headerName);
        if (StringUtils.isNotBlank(value)) {
            return value;
        }
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            final String name = headerNames.nextElement();
            if (StringUtils.equalsIgnoreCase(headerName, name)) {
                if (!StringUtils.equals(headerName, name)) {
                    log.info("http-header.key = {}", name);
                }
                return request.getHeader(name);
            }
        }
        return null;
    }

    public static String getHeader(HttpHeaders headers, String headerName) {
        final String value = headers.getFirst(headerName);
        if (StringUtils.isNotBlank(value)) {
            return value;
        }
        for (String name : headers.keySet()) {
            if (StringUtils.equalsIgnoreCase(headerName, name)) {
                if (!StringUtils.equals(headerName, name)) {
                    log.info("http-header.key = {}", name);
                }
                return headers.getFirst(headerName);
            }
        }
        return null;
    }

    public static String getRealIp(HttpHeaders headers) {
        return getRealIp(headers, () -> null);
    }

    public static String getRealIp(HttpHeaders headers, String defaultIp) {
        return getRealIp(headers, () -> defaultIp);
    }

    public static String getRealIp(HttpHeaders headers, Supplier<String> defaultIp) {
        final String realIp = getHeader(headers, "X-Real-IP");
        if (StringUtils.isNotBlank(realIp)) {
            return realIp;
        }
        final String forwardIp = getHeader(headers, "X-Forwarded-For");
        if (StringUtils.isNotBlank(forwardIp)) {
            return getFirstValue(forwardIp);
        }
        return defaultIp.get();
    }

    private static String getFirstValue(String httpHeaderValue) {
        if (!StringUtils.contains(httpHeaderValue, ",")) {
            return httpHeaderValue;
        }
        return StringUtils.trimToEmpty(StringUtils.substringBefore(httpHeaderValue, ","));
    }

}
