package com.lsnju.tpbase.log.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;

import com.lsnju.base.model.MaskJacksonUtils;
import com.lsnju.tpbase.config.prop.TpAopConfigProperties;
import com.lsnju.tpbase.log.AopSkipMethod;
import com.lsnju.tpbase.log.DigestConstants;
import com.lsnju.tpbase.log.aspectj.ProceedingJoinPointInterceptor;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author lsnju
 * @since 2025-09-29 18:05:17
 * @version V1.0
 * @since 3.1.19
 */
@Slf4j
public class TpRestApiLogInterceptor implements DigestConstants, ProceedingJoinPointInterceptor, AopSkipMethod {

    private static final Logger REST_LOG = LoggerFactory.getLogger(TP_REST_LOG);

    private final TpAopConfigProperties config;
    private final Set<String> skipMethodSet;
    private final Function<Object, String> toJsonStr;

    public TpRestApiLogInterceptor(TpAopConfigProperties config) {
        this(config, SKIP_METHOD);
    }

    public TpRestApiLogInterceptor(TpAopConfigProperties config, Set<String> skipMethodSet) {
        this(config, skipMethodSet, MaskJacksonUtils::toJson);
    }

    public TpRestApiLogInterceptor(TpAopConfigProperties config, Function<Object, String> toJsonStr) {
        this(config, SKIP_METHOD, toJsonStr);
    }

    public TpRestApiLogInterceptor(TpAopConfigProperties config, Set<String> skipMethodSet, Function<Object, String> toJsonStr) {
        this.config = config;
        this.skipMethodSet = skipMethodSet;
        this.toJsonStr = toJsonStr;
    }

    @Override
    public boolean skipDigest(String methodName) {
        return skipMethodSet.contains(methodName);
    }

    @Override
    public Object proceed(ProceedingJoinPoint joinPoint) throws Throwable {
        if (skipDigest(joinPoint.getSignature().getName())) {
            return joinPoint.proceed();
        }
        if (config == null) {
            return joinPoint.proceed();
        }
        if (!config.isEnableRestLog()) {
            return joinPoint.proceed();
        }
        if (config.isEnableRestLogReq()) {
            logRequest(joinPoint);
        }
        Object response = null;
        try {
            response = joinPoint.proceed();
            return response;
        } finally {
            if (config.isEnableRestLogResp()) {
                logResponse(response);
            }
        }
    }

    public void logRequest(ProceedingJoinPoint joinPoint) {
        try {
            REST_LOG.info("START_PROCESSING: {}", joinPoint.getSignature());
            List<String> argsList = new ArrayList<>();
            Object[] args = joinPoint.getArgs();
            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];
                if (arg != null) {
                    if (arg instanceof ServletResponse) {
                        continue;
                    }
                    if (arg instanceof ServletRequest) {
                        continue;
                    }
                    argsList.add(String.format("arg%d=%s", i, toJson(arg)));
                }
            }
            REST_LOG.info("REQ: {}", String.join(", ", argsList));
        } catch (Exception e) {
            log.error(String.format("%s", e.getMessage()), e);
        }
    }

    public void logResponse(Object response) {
        try {
            if (response == null) {
                REST_LOG.info("RESP: <null>");
                return;
            }
            if (response instanceof ServletResponse) {
                return;
            }
            if (response instanceof HttpEntity) {
                Object body = ((HttpEntity<?>) response).getBody();
                if (body != null) {
                    REST_LOG.info("RESP: {}", toJson(body));
                }
                return;
            }
            REST_LOG.info("RESP: {}", toJson(response));
        } catch (Exception e) {
            log.error(String.format("%s", e.getMessage()), e);
        }
    }

    private String toJson(Object response) {
        return toJsonStr.apply(response);
    }

}
