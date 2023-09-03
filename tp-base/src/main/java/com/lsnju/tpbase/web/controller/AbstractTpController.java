package com.lsnju.tpbase.web.controller;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.google.common.net.HttpHeaders;
import com.lsnju.base.util.ClazzUtils;
import com.lsnju.tpbase.config.prop.TpMoConfigProperties;
import com.lsnju.tpbase.web.filter.TpRequestFilter;
import com.lsnju.tpbase.web.mvc.TpSpringWebMvcHelper;
import com.lsnju.tpbase.web.util.TpHttpHeaderUtils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;

/**
 *
 * @author lisong
 * @since 2021/12/16 10:01
 * @version V1.0
 */
abstract class AbstractTpController {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    private static final String MSG = "[%s] The quick brown fox jumps over the lazy dog. [%s]";

    @Getter
    @Value("${spring.application.name:xxoo}")
    private String appName;
    @Autowired(required = false)
    protected TpSpringWebMvcHelper tpSpringWebMvcHelper;
    @Autowired
    protected TpMoConfigProperties tpMoConfigProperties;

    protected String getMsg() {
        return getMsg(StringUtils.EMPTY);
    }

    protected String getMsg(String tag) {
        return String.format(MSG, appName, tag);
    }

    protected void logInfo(HttpServletRequest request) {
        logInfo(request, false);
    }

    protected String getServerUrl() {
        if (tpSpringWebMvcHelper != null) {
            return tpSpringWebMvcHelper.getServerUrl();
        }
        return StringUtils.EMPTY;
    }

    protected void logInfo(HttpServletRequest request, boolean debug) {
        if (log.isInfoEnabled()) {
            if (debug) {
                log.info("X_FORWARDED_PROTO : {}", TpHttpHeaderUtils.getHeader(request, HttpHeaders.X_FORWARDED_PROTO));
                log.info("X_FORWARDED_HOST  : {}", TpHttpHeaderUtils.getHeader(request, HttpHeaders.X_FORWARDED_HOST));
                log.info("X_FORWARDED_PORT  : {}", TpHttpHeaderUtils.getHeader(request, HttpHeaders.X_FORWARDED_PORT));
                log.info("X_FORWARDED_FOR   : {}", TpHttpHeaderUtils.getHeader(request, HttpHeaders.X_FORWARDED_FOR));
                log.info("X_REAL_IP         : {}", TpHttpHeaderUtils.getHeader(request, TpRequestFilter.X_REAL_IP));
                log.info("HOST              : {}", TpHttpHeaderUtils.getHeader(request, HttpHeaders.HOST));
                log.info("FORWARDED         : {}", TpHttpHeaderUtils.getHeader(request, HttpHeaders.FORWARDED));
                log.info("getServerName     : {}", request.getServerName());
                log.info("getServerPort     : {}", request.getServerPort());
                log.info("getScheme         : {}", request.getScheme());
                log.info("getRemoteHost     : {}", request.getRemoteHost());
                log.info("getRemotePort     : {}", request.getRemotePort());
                log.info("getProtocol       : {}", request.getProtocol());
                log.info("getContextPath    : {}", request.getContextPath());
                log.info("USER_AGENT        : {}", TpHttpHeaderUtils.getUserAgent(request));
            }
            log.info("url={}", getServerUrl());
            log.info("url={}", request.getRequestURL());
            log.info("dir={}", ClazzUtils.getWorkingDir());
        }
    }

}
