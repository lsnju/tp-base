package com.lsnju.tpbase.web.filter.rest;

import java.io.IOException;
import java.util.Objects;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lsnju.base.model.rs.TpRestContext;
import com.lsnju.tpbase.config.FilterOrderConstants;
import com.lsnju.tpbase.log.DigestConstants;
import com.lsnju.tpbase.web.filter.AbstractTpRequestFilter;

/**
 *
 * @author ls
 * @since 2024/10/1 19:43
 * @version V1.0
 */
public class TpRestApiDigestFilter extends AbstractTpRequestFilter implements DigestConstants, FilterOrderConstants {

    private static final Logger DIGEST_LOGGER = LoggerFactory.getLogger(TP_REST_API);
    private static final String DIGEST_FORMAT = "[%s,%s,%sms,rc_%s] %s";
    public static final String DEFAULT = "na";

    @Override
    public int getOrder() {
        return RS_API_ORDER;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        TpRestContext.clear();
        final long startTime = System.nanoTime();
        try {
            filterChain.doFilter(request, response);
        } finally {
            if (DIGEST_LOGGER.isInfoEnabled()) {
                final long costTime = (System.nanoTime() - startTime) / DigestConstants.MS_SCALE;
                final String msg = String.format(DIGEST_FORMAT, request.getMethod(), request.getServletPath(), costTime,
                    getRsCode(), getRsMsg());
                DIGEST_LOGGER.info(msg);
            }
            TpRestContext.clear();
        }
    }

    private static String getRsMsg() {
        return Objects.toString(TpRestContext.getRsMsg(), DEFAULT);
    }

    private static String getRsCode() {
        return Objects.toString(TpRestContext.getRsCode(), DEFAULT);
    }

}
