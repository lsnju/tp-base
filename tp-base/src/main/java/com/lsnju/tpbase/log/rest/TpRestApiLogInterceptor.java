package com.lsnju.tpbase.log.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;

import com.lsnju.base.jackson.JacksonUtils;
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

    private final Set<String> skipMethodSet;

    public TpRestApiLogInterceptor() {
        this.skipMethodSet = SKIP_METHOD;
    }

    public TpRestApiLogInterceptor(Set<String> skipMethodSet) {
        this.skipMethodSet = skipMethodSet;
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

        logRequest(joinPoint);
        Object response = null;
        try {
            response = joinPoint.proceed();
            return response;
        } finally {
            logResponse(response);
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
                    argsList.add(String.format("arg%d=%s", i, JacksonUtils.toJson(arg)));
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
                    REST_LOG.info("RESP: {}", JacksonUtils.toJson(body));
                }
                return;
            }
            REST_LOG.info("RESP: {}", JacksonUtils.toJson(response));
        } catch (Exception e) {
            log.error(String.format("%s", e.getMessage()), e);
        }
    }

}
