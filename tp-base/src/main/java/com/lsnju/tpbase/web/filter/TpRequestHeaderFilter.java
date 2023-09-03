package com.lsnju.tpbase.web.filter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lsnju.tpbase.log.DigestConstants;

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
 * @since 2021/5/7 17:02
 * @version V1.0
 */
@Slf4j
public class TpRequestHeaderFilter implements Filter {

    private static final Logger HTTP_LOGGER = LoggerFactory.getLogger(DigestConstants.TP_HTTP_HEADER);

    public static final String IN = ">>  ";
    public static final String OUT = "<<  ";
    public static final String SPLIT = ": ";
    public static final String NL = "\n";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.debug("RequestHeaderFilter.init {}", filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            logRequest(request);
            chain.doFilter(request, response);
        } finally {
            logResponse(response);
        }
    }

    private void logRequest(ServletRequest request) {
        if (request instanceof HttpServletRequest && HTTP_LOGGER.isInfoEnabled()) {
            HttpServletRequest req = (HttpServletRequest) request;
            StringBuilder sb = new StringBuilder();
            Set<String> names = new TreeSet<>();

            Enumeration<String> headerNames = req.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                names.add(headerNames.nextElement());
            }

            for (String name : names) {
                Enumeration<String> values = req.getHeaders(name);
                while (values.hasMoreElements()) {
                    String value = values.nextElement();
                    sb.append(IN).append(name).append(SPLIT).append(value).append(NL);
                }
            }
            HTTP_LOGGER.info(">>>\n{}", sb.toString());
        }
    }

    private void logResponse(ServletResponse response) {
        if (response instanceof HttpServletResponse && HTTP_LOGGER.isInfoEnabled()) {
            HttpServletResponse resp = (HttpServletResponse) response;
            StringBuilder sb = new StringBuilder();
            sb.append(OUT).append("code").append(SPLIT).append(resp.getStatus()).append(NL);
            for (String name : new TreeSet<>(resp.getHeaderNames())) {
                for (String value : resp.getHeaders(name)) {
                    sb.append(OUT).append(name).append(SPLIT).append(value).append(NL);
                }
            }
            HTTP_LOGGER.info("<<<\n{}", sb.toString());
        }
    }
}
