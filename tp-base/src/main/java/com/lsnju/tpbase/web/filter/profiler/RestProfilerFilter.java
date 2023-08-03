package com.lsnju.tpbase.web.filter.profiler;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.lsnju.tpbase.log.DigestConstants;
import com.lsnju.base.util.Profiler;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author ls
 * @since 2023-07-26 20:23:12
 * @version V1.0
 */
@Slf4j
@Setter
public class RestProfilerFilter implements Filter, DigestConstants {

    private static final Logger PROFILER_LOGGER = LoggerFactory.getLogger(DigestConstants.TP_PROFILER);

    @Value("${rest.api.profiler.timeout:1000}")
    private long apiTimeout = 5000L;

    @Override
    public void init(FilterConfig filterConfig) {
        log.info("{} 超时阈值：{}ms", this, apiTimeout);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            HttpServletRequest req = (HttpServletRequest) request;
            try {
                Profiler.start(req.getRequestURI());
                chain.doFilter(request, response);
            } finally {
                Profiler.release();
                if (PROFILER_LOGGER.isInfoEnabled() && Profiler.getDuration() > apiTimeout) {
                    PROFILER_LOGGER.info("\n{}\n", Profiler.dump());
                }
                Profiler.reset();
            }
        }
    }

    @Override
    public void destroy() {
    }
}
