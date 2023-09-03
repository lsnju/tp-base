package com.lsnju.tpbase.web.filter.rest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.lsnju.base.enums.BizErrorEnum;
import com.lsnju.base.model.rs.BaseResp;
import com.lsnju.base.util.JsonUtils;
import com.lsnju.tpbase.web.auth.PageMatcher;
import com.lsnju.tpbase.web.util.OperationContext;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ls
 * @since 2020/11/27 10:09
 * @version V1.0
 */
@Slf4j
@Setter
public class RestApiAuthFilter implements Filter {

    public static final String TOKEN_ID = "token_id";
    private static final String NO_AUTH = "NO_AUTH";

    @Setter(AccessLevel.PRIVATE)
    private PageMatcher pm = null;

    @Autowired
    private RestApiTokenManager restApiTokenManager;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            final HttpServletRequest req = (HttpServletRequest) request;
            final HttpServletResponse resp = (HttpServletResponse) response;

            final String requestUrl = getRequestUrl(req);
            log.info("url={}", requestUrl);

            if (isNoAuthPage(requestUrl)) {
                chain.doFilter(request, response);
                return;
            }

            String tokenId = req.getHeader(TOKEN_ID);
            if (!restApiTokenManager.isValid(tokenId)) {
                sendAuthError(resp, "无效 token");
                return;
            }

            final Set<String> roleSet = OperationContext.getUserInfo().getRoleSet();
            if (!hasPermission(requestUrl, roleSet)) {
                sendAuthError(resp, "无权访问该资源");
                return;
            }

            try {
                chain.doFilter(req, response);
            } finally {
                OperationContext.clear();
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    private void sendAuthError(HttpServletResponse resp, String msg) throws IOException {
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resp.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        resp.getWriter().write(JsonUtils.toJson(BaseResp.of(null, BizErrorEnum.NO_AUTH, msg)));
        resp.setStatus(HttpStatus.UNAUTHORIZED.value());
    }

    private String getRequestUrl(HttpServletRequest req) {
        String uri = req.getRequestURI();
        uri = RegExUtils.replaceAll(uri, "/+", "/");
        uri = StringUtils.substringBefore(uri, ";");
        log.debug("uri={}", uri);

        final String contextPath = req.getContextPath();
        return StringUtils.isBlank(contextPath) ? uri : StringUtils.substringAfter(uri, contextPath);
    }

    private boolean isNoAuthPage(String url) {
        String[] perms = pm.getUrlPermConf(url);
        return ArrayUtils.contains(perms, NO_AUTH);
    }

    private boolean hasPermission(String url, Set<String> roleSet) {
        String[] perms = pm.getUrlPermConf(url);
        for (String role : roleSet) {
            boolean v = ArrayUtils.contains(perms, role);
            if (v) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("init");
        WebApplicationContext acx =
            WebApplicationContextUtils.getWebApplicationContext(filterConfig.getServletContext());
        try {
            Assert.notNull(acx, "WebApplicationContext is null");
            Resource[] rs = acx.getResources("classpath:/cc/sec/default.acf");
            for (Resource r : rs) {
                log.debug("{}", r);
            }
            pm = new PageMatcher(rs);
            log.debug("pm={}", pm.getGroups());
        } catch (IOException e) {
            log.error(String.format("%s", e.getMessage()), e);
            throw new ServletException(e);
        }
    }

    @Override
    public void destroy() {
    }
}
