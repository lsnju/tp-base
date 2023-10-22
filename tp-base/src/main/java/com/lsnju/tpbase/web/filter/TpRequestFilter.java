package com.lsnju.tpbase.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.joor.Reflect;
import org.joor.ReflectException;
import org.slf4j.MDC;

import com.lsnju.base.util.UUIDGenerator;
import com.lsnju.tpbase.config.LogMdcConstants;
import com.lsnju.tpbase.config.prop.TpFilterConfigProperties;
import com.lsnju.tpbase.util.VersionConfig;
import com.lsnju.tpbase.web.util.OperationContext;
import com.lsnju.tpbase.web.util.RequestUtils;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author ls
 * @since 2023-07-26 21:34:49
 * @version V1.0
 */
@Slf4j
public class TpRequestFilter implements Filter {

    private static final String HOST_NAME = "X-tp-hn";
    private static final String TOKEN = "X-tp-token";

    static final String ALREADY_FILTERED_ATTRIBUTE = TpRequestFilter.class.getName() + ".FILTERED";

    private final TpFilterConfigProperties tpFilterConfigProperties;

    public TpRequestFilter(TpFilterConfigProperties tpFilterConfigProperties) {
        this.tpFilterConfigProperties = tpFilterConfigProperties;
    }

    @Override
    public void init(FilterConfig filterConfig) {
        log.debug("RequestFilter.init {}", filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request.getAttribute(ALREADY_FILTERED_ATTRIBUTE) != null) {
            chain.doFilter(request, response);
        } else {
            request.setAttribute(ALREADY_FILTERED_ATTRIBUTE, Boolean.TRUE);

            if (request instanceof HttpServletRequest) {
                final HttpServletRequest req = (HttpServletRequest) request;
                final HttpServletResponse resp = (HttpServletResponse) response;
                try {
                    // ...
                    final String remoteIp = RequestUtils.getRequestIp(req);
                    MDC.put(LogMdcConstants.REQUEST_REMOTE_IP_MDC_KEY, remoteIp);
                    if (tpFilterConfigProperties.isOverrideRemoteHost()) {
                        MDC.put(LogMdcConstants.REQUEST_REMOTE_HOST_MDC_KEY, remoteIp);
                    }
                    MDC.put(RequestId.MDC_REQ_ID, getReqId(request));
                    setupResp(resp);
                    if (log.isDebugEnabled()) {
                        log.debug("uri={}, url={}", req.getRequestURI(), req.getRequestURL());
                        showFilterChain(chain);
                    }
                    chain.doFilter(request, response);
                } finally {
                    MDC.remove(RequestId.MDC_REQ_ID);
                    MDC.remove(LogMdcConstants.REQUEST_REMOTE_IP_MDC_KEY);
                    OperationContext.clear();
                }
            } else {
                chain.doFilter(request, response);
            }
        }
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

    private void setupResp(HttpServletResponse resp) {
        if (tpFilterConfigProperties.isAddResponse()) {
            resp.addHeader(HOST_NAME, VersionConfig.getHostname());
            resp.addHeader(TOKEN, MDC.get(RequestId.MDC_REQ_ID));
        }
    }

    private String getReqId(ServletRequest request) {
        if (!tpFilterConfigProperties.isTraceHeader()) {
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
