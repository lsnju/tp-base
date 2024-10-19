package com.lsnju.tpbase.web.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lsnju.tpbase.log.DigestConstants;
import com.lsnju.tpbase.web.util.RequestUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 页面请求性能日志输出
 *
 * @author ls
 * @since 2023-07-26 21:16:05
 * @version V1.0
 */
@Slf4j
public class TpPagePerfFilter implements Filter, DigestConstants {

    /** */
    private static final Logger PAGE_PERF_LOGGER = LoggerFactory.getLogger(TP_PAGE_PERF);

    private static final Logger WEBX_LOGGER = LoggerFactory.getLogger(WEB_X);

    /** [method,uri,cost,result,ip] */
    private static final String PAGE_DIGEST_FORMAT = "[%s,%s,%sms,%s,%s] %s";

    @Override
    public void init(FilterConfig filterConfig) {
        log.debug("WebPerfFilter.init filterCondfig={}", filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        log.debug("PagePerfFilter.doFilter req={},resp={},chain={}", request, response, chain);
        if (request instanceof HttpServletRequest) {
            HttpServletRequest req = (HttpServletRequest) request;
            HttpServletResponse resp = (HttpServletResponse) response;

            final String uri = req.getRequestURI();
            final String method = req.getMethod();

            boolean success = true;
            final long startTime = System.nanoTime();
            try {
                chain.doFilter(request, response);
            } catch (Throwable e) {
                success = false;
                log.error(String.format("【http异常】error=%s", e.getMessage()), e);
                throw e;
            } finally {
                if (PAGE_PERF_LOGGER.isInfoEnabled()) {
                    final long costTime = (System.nanoTime() - startTime) / DigestConstants.MS_SCALE;
                    String msg = String.format(PAGE_DIGEST_FORMAT, method, uri, costTime, success ? "S" : "E",
                        RequestUtils.getRequestIp(), resp.getStatus());
                    PAGE_PERF_LOGGER.info(msg);
                }
                WEBX_LOGGER.info("{} ", resp.getStatus());
            }
        } else {
            try {
                chain.doFilter(request, response);
            } finally {
                WEBX_LOGGER.info("x ");
            }
        }
    }

    @Override
    public void destroy() {
        log.info("WebPerfFilter.destroy");
    }
}
