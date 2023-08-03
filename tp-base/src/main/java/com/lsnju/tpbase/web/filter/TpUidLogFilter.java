package com.lsnju.tpbase.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import com.lsnju.tpbase.web.util.OperationContext;

/**
 * @author ls
 * @since 2020/11/27 10:03
 * @version V1.0
 */
public class TpUidLogFilter implements Filter {

    public static final String SESSION_UID = "session_uid";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        try {
            final String uid = OperationContext.getUid();
            if (StringUtils.isNotBlank(uid)) {
                MDC.put(SESSION_UID, uid);
            }
            chain.doFilter(request, response);
        } finally {
            MDC.remove(SESSION_UID);
        }
    }
}
