package com.lsnju.tpbase.web.filter.reactive;

import java.util.Optional;
import java.util.function.Consumer;

import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.lang.NonNull;
import org.springframework.web.server.ServerWebExchange;

import com.lsnju.tpbase.config.LogMdcConstants;
import com.lsnju.tpbase.web.filter.RequestId;
import com.lsnju.tpbase.web.util.TpHttpHeaderUtils;

import reactor.core.publisher.Signal;

/**
 *
 * @author ls
 * @since 2021/5/8 9:10
 * @version V1.0
 */
public class RxLogUtils {

    public static void logWithContext(@NonNull ServerWebExchange exchange, @NonNull Runnable logAction) {
        logWithContext(exchange, exchange.getAttribute(RequestId.RX_CTX_ID), logAction);
    }

    public static void logWithContext(@NonNull ServerWebExchange exchange, String requestId, @NonNull Runnable logAction) {
        try {
            MDC.put(RequestId.MDC_REQ_ID, requestId);
            insertIntoMDC(exchange);
            logAction.run();
        } finally {
            clearMDC();
            MDC.remove(RequestId.MDC_REQ_ID);
        }
    }

    // ch.qos.logback.classic.helpers.MDCInsertingServletFilter
    static void insertIntoMDC(ServerWebExchange exchage) {
        ServerHttpRequest request = exchage.getRequest();
        HttpHeaders headers = request.getHeaders();
        if (request.getRemoteAddress() != null) {
            String remoteHost = request.getRemoteAddress().getHostString();
            String realIp = TpHttpHeaderUtils.getRealIp(headers, remoteHost);
            MDC.put(LogMdcConstants.REQUEST_REMOTE_HOST_MDC_KEY, realIp);
            MDC.put(LogMdcConstants.REQUEST_X_FORWARDED_FOR, TpHttpHeaderUtils.getHeader(headers, com.google.common.net.HttpHeaders.X_FORWARDED_FOR));
        }
        MDC.put(LogMdcConstants.REQUEST_REQUEST_URI, request.getURI().getPath());
        MDC.put(LogMdcConstants.REQUEST_QUERY_STRING, request.getURI().getRawQuery());
        MDC.put(LogMdcConstants.REQUEST_REQUEST_URL, request.getURI().toString());
        MDC.put(LogMdcConstants.REQUEST_METHOD, request.getMethod().name());
        MDC.put(LogMdcConstants.REQUEST_USER_AGENT_MDC_KEY, TpHttpHeaderUtils.getUserAgent(headers));
    }

    static void clearMDC() {
        MDC.remove(LogMdcConstants.REQUEST_REMOTE_HOST_MDC_KEY);
        MDC.remove(LogMdcConstants.REQUEST_REQUEST_URI);
        MDC.remove(LogMdcConstants.REQUEST_QUERY_STRING);
        MDC.remove(LogMdcConstants.REQUEST_REQUEST_URL);
        MDC.remove(LogMdcConstants.REQUEST_METHOD);
        MDC.remove(LogMdcConstants.REQUEST_USER_AGENT_MDC_KEY);
        MDC.remove(LogMdcConstants.REQUEST_X_FORWARDED_FOR);
    }

    public static Consumer<Signal<?>> logOnError(Consumer<Throwable> errorLogStatement) {
        return signal -> {
            if (!signal.isOnError()) {
                return;
            }
            Optional<String> toPutInMdc = signal.getContextView().getOrEmpty(RequestId.RX_CTX_ID);
            if (toPutInMdc.isPresent()) {
                try (MDC.MDCCloseable ignored = MDC.putCloseable(RequestId.MDC_REQ_ID, toPutInMdc.get())) {
                    errorLogStatement.accept(signal.getThrowable());
                }
            } else {
                errorLogStatement.accept(signal.getThrowable());
            }
        };
    }

    public static <T> Consumer<Signal<T>> logOnNext(Consumer<T> logStatement) {
        return signal -> {
            if (!signal.isOnNext()) return;
            Optional<String> toPutInMdc = signal.getContextView().getOrEmpty(RequestId.RX_CTX_ID);
            if (toPutInMdc.isPresent()) {
                try (MDC.MDCCloseable ignored = MDC.putCloseable(RequestId.MDC_REQ_ID, toPutInMdc.get())) {
                    logStatement.accept(signal.get());
                }
            } else {
                logStatement.accept(signal.get());
            }
        };
    }

}
