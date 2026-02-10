package com.lsnju.tpbase.log.rest;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.InputStreamSource;
import org.springframework.http.HttpEntity;

import com.lsnju.base.model.MaskJacksonUtils;
import com.lsnju.tpbase.config.prop.TpAopConfigProperties;
import com.lsnju.tpbase.log.AopSkipMethod;
import com.lsnju.tpbase.log.DigestConstants;
import com.lsnju.tpbase.log.annotation.TpSkipLog;
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

        if (joinPoint.getSignature() instanceof MethodSignature) {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            TpSkipLog clazzAnnotation = AnnotationUtils.findAnnotation(method.getDeclaringClass(), TpSkipLog.class);
            if (clazzAnnotation != null) {
                log.debug("class_has_annotation_TpSkipLog");
                return joinPoint.proceed();
            }

            TpSkipLog methodAnnotation = AnnotationUtils.findAnnotation(method, TpSkipLog.class);
            if (methodAnnotation != null) {
                log.debug("method_has_annotation_TpSkipLog");
                return joinPoint.proceed();
            }
        } else {
            log.debug("joinPoint_is_not_MethodSignature");
            return joinPoint.proceed();
        }

        if (config == null) {
            return joinPoint.proceed();
        }
        if (!config.isEnableRestLog()) {
            return joinPoint.proceed();
        }
        String reqStr = null;
        if (config.isEnableRestLogReq()) {
            reqStr = logRequest(joinPoint);
        }
        Object response = null;
        try {
            response = joinPoint.proceed();
            return response;
        } finally {
            if (config.isEnableRestLogResp()) {
                logResponse(response, reqStr);
            }
        }
    }

    public String logRequest(ProceedingJoinPoint joinPoint) {
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
                    if (arg instanceof InputStreamSource) {
                        continue;
                    }
                    argsList.add(String.format("arg%d=%s", i, toJson(arg)));
                }
            }
            String reqStr = String.join(", ", argsList);
            REST_LOG.info("REQ: {}", reqStr);
            return reqStr;
        } catch (Exception e) {
            log.error(String.format("%s", e.getMessage()), e);
            return StringUtils.EMPTY;
        }
    }

    public void logResponse(Object response, String reqStr) {
        try {
            if (response == null) {
                REST_LOG.info("RESP: <null>; IN: {}", reqStr);
                return;
            }
            if (response instanceof ServletResponse) {
                REST_LOG.info("RESP: <ServletResponse>; IN: {}", reqStr);
                return;
            }
            if (response instanceof HttpEntity) {
                Object body = ((HttpEntity<?>) response).getBody();
                if (body != null) {
                    REST_LOG.info("RESP: {}; IN: {}", toJson(body), reqStr);
                }
                return;
            }
            REST_LOG.info("RESP: {}; IN: {}", toJson(response), reqStr);
        } catch (Exception e) {
            log.error(String.format("%s", e.getMessage()), e);
        }
    }

    private String toJson(Object response) {
        return toJsonStr.apply(response);
    }

}
