package com.lsnju.tpbase.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.MDC;

import com.lsnju.tpbase.config.LogMdcConstants;
import com.lsnju.tpbase.web.util.RequestUtils;


/**
 *
 * @author ls
 * @since 2023/9/16 12:34
 * @version V1.0
 */
public class TpMDCInsertingServletFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        insertIntoMDC(request);
        try {
            chain.doFilter(request, response);
        } finally {
            clearMDC();
        }
    }

    void insertIntoMDC(ServletRequest request) {

        MDC.put(LogMdcConstants.REQUEST_REMOTE_HOST_MDC_KEY, request.getRemoteHost());

        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            MDC.put(LogMdcConstants.REQUEST_REMOTE_IP_MDC_KEY, RequestUtils.getRequestIp(httpServletRequest));
            MDC.put(LogMdcConstants.REQUEST_REQUEST_URI, httpServletRequest.getRequestURI());
            StringBuffer requestURL = httpServletRequest.getRequestURL();
            if (requestURL != null) {
                MDC.put(LogMdcConstants.REQUEST_REQUEST_URL, requestURL.toString());
            }
            MDC.put(LogMdcConstants.REQUEST_METHOD, httpServletRequest.getMethod());
            MDC.put(LogMdcConstants.REQUEST_QUERY_STRING, httpServletRequest.getQueryString());
            MDC.put(LogMdcConstants.REQUEST_USER_AGENT_MDC_KEY, httpServletRequest.getHeader("User-Agent"));
            MDC.put(LogMdcConstants.REQUEST_X_FORWARDED_FOR, httpServletRequest.getHeader("X-Forwarded-For"));
            MDC.put(LogMdcConstants.REQUEST_SERVLET_PATH, httpServletRequest.getServletPath());
        }

    }

    void clearMDC() {
        MDC.remove(LogMdcConstants.REQUEST_REMOTE_HOST_MDC_KEY);
        MDC.remove(LogMdcConstants.REQUEST_REMOTE_IP_MDC_KEY);
        MDC.remove(LogMdcConstants.REQUEST_REQUEST_URI);
        MDC.remove(LogMdcConstants.REQUEST_QUERY_STRING);
        MDC.remove(LogMdcConstants.REQUEST_REQUEST_URL);
        MDC.remove(LogMdcConstants.REQUEST_METHOD);
        MDC.remove(LogMdcConstants.REQUEST_USER_AGENT_MDC_KEY);
        MDC.remove(LogMdcConstants.REQUEST_X_FORWARDED_FOR);
        MDC.remove(LogMdcConstants.REQUEST_SERVLET_PATH);
    }

    @Override
    public void destroy() {

    }
}
