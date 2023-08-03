package com.lsnju.tpbase.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.lsnju.tpbase.web.model.TpUserInfo;
import com.lsnju.tpbase.web.util.OperationContext;
import com.lsnju.tpbase.web.util.TpSessionUtils;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author ls
 * @since 2023-07-26 21:34:01
 * @version V1.0
 */
@Slf4j
public class TpSessionFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        log.debug("[XX-start] SessionFilter req={}", request);
        // session中获取用户ID
        try {
            // 设置当前用户信息
            if (request instanceof HttpServletRequest) {
                HttpServletRequest req = (HttpServletRequest) request;
                String uid = TpSessionUtils.getUid(req);
                if (StringUtils.isNotBlank(uid)) {
                    // 设置上下文用户信息
                    TpUserInfo userInfo = TpSessionUtils.getUserInfo(req);

                    OperationContext.setUid(uid);
                    OperationContext.setUserInfo(userInfo);
                }
            }
            chain.doFilter(request, response);
        } finally {
            log.debug("[XX-end] SessionFilter req={}", request);
            OperationContext.clear();
        }
    }

    @Override
    public void destroy() {
        log.info("destroy()");
    }
}
