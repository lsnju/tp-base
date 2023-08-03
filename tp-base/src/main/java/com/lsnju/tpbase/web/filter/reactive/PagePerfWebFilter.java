package com.lsnju.tpbase.web.filter.reactive;

import java.net.InetSocketAddress;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.lsnju.tpbase.log.DigestConstants;
import com.lsnju.tpbase.web.filter.RequestId;
import com.lsnju.tpbase.web.util.TpHttpHeaderUtils;
import com.lsnju.base.util.UUIDGenerator;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Signal;
import reactor.util.context.Context;

/**
 * @author lisong
 * @since 2020/2/16 11:10
 * @version V1.0
 */
@Slf4j
public class PagePerfWebFilter implements WebFilter, Ordered {

    private static final Logger PAGE_PERF_LOGGER = LoggerFactory.getLogger(DigestConstants.TP_PAGE_PERF);
    private static final String FORMAT = "[%s,%s,%dms,%s,%s] %s %s";

    private static final Logger HTTP_LOGGER = LoggerFactory.getLogger(DigestConstants.TP_HTTP_HEADER);

    public static final String IN = ">>  ";
    public static final String OUT = "<<  ";
    public static final String SPLIT = ": ";
    public static final String NL = "\n";

    @Override
    public int getOrder() {
        log.debug("PagePerfWebFilter.getOrder");
        return Ordered.HIGHEST_PRECEDENCE + 300;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        final ServerHttpRequest request = exchange.getRequest();
        final String path = request.getURI().getPath();
        final String method = request.getMethodValue();
        final String requestId = getRequestId(exchange);
        exchange.getAttributes().put(RequestId.RX_CTX_ID, requestId);

        RxLogUtils.logWithContext(exchange, requestId, () -> logRequest(exchange));
        final long start = System.nanoTime();
        return chain.filter(exchange)
            .doOnEach(s -> logEach(s, exchange, requestId, method, path, start))
            .contextWrite(Context.of(RequestId.RX_CTX_ID, requestId));
    }

    private String getRequestId(ServerWebExchange exchange) {
        String reqId = exchange.getRequest().getHeaders().getFirst(RequestId.HTTP_REQ_ID);
        if (StringUtils.isNotBlank(reqId)) {
            return reqId;
        }
        String newReqId = UUIDGenerator.getSUID();
        exchange.getRequest().mutate().header(RequestId.HTTP_REQ_ID, newReqId).build();
        return newReqId;
    }

    private void logEach(Signal<Void> signal, ServerWebExchange exchange, String requestId, String method, String path, long start) {
        RxLogUtils.logWithContext(exchange, requestId, () -> {
            logDigest(exchange, method, path, start, isError(signal) ? "E" : "S");
        });
    }

    private boolean isError(Signal<Void> signal) {
        if (signal.hasError()) {
            Throwable exception = signal.getThrowable();
            if (exception != null) {
                log.error("exception_msg = {}", exception.getMessage());
            }
            return true;
        }
        return false;
    }

    private void logDigest(ServerWebExchange exchange, String method, String path, long start, String s) {
        if (PAGE_PERF_LOGGER.isInfoEnabled()) {
            final long cost = System.nanoTime() - start;
            final String logPrefix = exchange.getLogPrefix();
            final HttpStatus httpStatus = exchange.getResponse().getStatusCode();
            PAGE_PERF_LOGGER.info(String.format(FORMAT, method, path, cost / DigestConstants.MS_SCALE, s, getIp(exchange),
                logPrefix, httpStatus));
        }
        logResponse(exchange);
    }

    private String getIp(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders headers = request.getHeaders();
        String forwardIp = TpHttpHeaderUtils.getRealIp(headers);
        if (StringUtils.isNotBlank(forwardIp)) {
            return forwardIp;
        }
        InetSocketAddress remoteAddress = request.getRemoteAddress();
        if (remoteAddress != null) {
            return remoteAddress.getHostString();
        }
        return StringUtils.EMPTY;
    }

    private void logRequest(ServerWebExchange exchange) {
        final ServerHttpRequest request = exchange.getRequest();
        final String logPrefix = exchange.getLogPrefix();
        if (HTTP_LOGGER.isInfoEnabled()) {
            StringBuilder sb = new StringBuilder();
            HttpHeaders headers = request.getHeaders();
            headers.forEach((name, values) -> values.forEach(value -> sb.append(IN).append(name).append(SPLIT).append(value).append(NL)));
            HTTP_LOGGER.info("{}>>>\n{}", logPrefix, sb.toString());
        }
    }

    private void logResponse(ServerWebExchange exchange) {
        final ServerHttpResponse response = exchange.getResponse();
        final String logPrefix = exchange.getLogPrefix();
        if (HTTP_LOGGER.isInfoEnabled()) {
            StringBuilder sb = new StringBuilder();
            sb.append(OUT).append("code").append(SPLIT).append(response.getStatusCode()).append(NL);
            HttpHeaders headers = response.getHeaders();
            headers.forEach((name, values) -> values.forEach(value -> sb.append(OUT).append(name).append(SPLIT).append(value).append(NL)));
            HTTP_LOGGER.info("{}<<<\n{}", logPrefix, sb.toString());
        }
    }

}
