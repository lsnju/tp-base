package com.lsnju.tpbase.web.filter;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.joor.Reflect;
import org.joor.ReflectException;
import org.slf4j.MDC;

import com.lsnju.base.util.UUIDGenerator;
import com.lsnju.tpbase.util.VersionConfig;
import com.lsnju.tpbase.web.util.OperationContext;

import ch.qos.logback.classic.ClassicConstants;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author ls
 * @since 2023-07-26 21:34:49
 * @version V1.0
 */
@Slf4j
public class TpRequestFilter implements Filter {

    /** */
    @Deprecated
    public static final String REQ_ID = RequestId.HTTP_REQ_ID;

    public static final String HOST_NAME = "X_tp_hn";
    public static final String TOKEN = "X_tp_token";
    public static final String X_REAL_IP = "X-Real-IP";

    private final boolean useHeader;

    public TpRequestFilter() {
        this(true);
    }

    public TpRequestFilter(boolean useHeader) {
        this.useHeader = useHeader;
    }

    @Override
    public void init(FilterConfig filterConfig) {
        log.debug("RequestFilter.init {}", filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        try {
            // ...
            final String ip = getRealIp(request);
            if (StringUtils.isNotBlank(ip)) {
                MDC.put(ClassicConstants.REQUEST_REMOTE_HOST_MDC_KEY, StringUtils.trimToEmpty(ip));
            }
            // ...
            MDC.put(RequestId.MDC_REQ_ID, getReqId(request));
            setupResp(response);
            if (log.isDebugEnabled()) {
                log.debug("uri={}, url={}", MDC.get(ClassicConstants.REQUEST_REQUEST_URI), MDC.get(ClassicConstants.REQUEST_REQUEST_URL));
                showFilterChain(chain);
            }
            chain.doFilter(request, response);
        } finally {
            MDC.remove(RequestId.MDC_REQ_ID);
            OperationContext.clear();
        }
    }

    private String getRealIp(ServletRequest request) {
        if (request instanceof HttpServletRequest) {
            HttpServletRequest req = (HttpServletRequest) request;
            final String realIp = req.getHeader(X_REAL_IP);
            if (StringUtils.isNotBlank(realIp)) {
                return realIp;
            }
        }
        // "X-Forwarded-For"
        final String forwardIp = MDC.get(ClassicConstants.REQUEST_X_FORWARDED_FOR);
        if (StringUtils.isNotBlank(forwardIp)) {
            return StringUtils.trimToEmpty(StringUtils.substringBefore(forwardIp, ","));
        }
        return null;
    }

    private void showFilterChain(FilterChain chain) {
        try {
            final Reflect reflect = Reflect.on(chain).field("filters");
            final Class<?> type = reflect.type();
            log.debug("type={}", type);
            if (type.isArray()) {
                Object[] array = reflect.get();
                for (Object obj : array) {
                    if (obj == null) {
                        continue;
                    }
                    log.debug("{}", obj);
                }
            } else if (Iterable.class.isAssignableFrom(type)) {
                Iterable<?> it = reflect.get();
                for (Object obj : it) {
                    if (obj == null) {
                        continue;
                    }
                    log.debug("{}", obj);
                }
            }
        } catch (ReflectException e) {
            log.error(String.format("%s", e.getMessage()), e);
        }
    }

    private void setupResp(ServletResponse response) {
        if (response instanceof HttpServletResponse) {
            HttpServletResponse resp = (HttpServletResponse) response;
            resp.addHeader(HOST_NAME, VersionConfig.getHostname());
            resp.addHeader(TOKEN, MDC.get(RequestId.MDC_REQ_ID));
        }
    }

    private String getReqId(ServletRequest request) {
        if (!useHeader) {
            return UUIDGenerator.getSUID();
        }
        if (request instanceof HttpServletRequest) {
            HttpServletRequest req = (HttpServletRequest) request;
            final String headerId = req.getHeader(RequestId.HTTP_REQ_ID);
            if (StringUtils.isNotBlank(headerId)) {
                return StringUtils.substring(headerId, -16);
            }
        }
        return UUIDGenerator.getSUID();
    }

    @Override
    public void destroy() {
        log.warn("RequsetFilter.destroy");
    }
}
