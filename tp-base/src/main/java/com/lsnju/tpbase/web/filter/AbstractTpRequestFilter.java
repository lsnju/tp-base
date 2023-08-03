package com.lsnju.tpbase.web.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.filter.OrderedFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import com.lsnju.base.enums.BizErrEnum;
import com.lsnju.base.model.rs.BaseResp;
import com.lsnju.base.util.JsonUtils;

/**
 *
 * @author ls
 * @since 2021/8/25 19:53
 * @version V1.0
 */
public abstract class AbstractTpRequestFilter extends OncePerRequestFilter implements OrderedFilter {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return !shouldFilter(Objects.requireNonNull(request));
    }

    protected boolean shouldFilter(HttpServletRequest request) {
        return true;
    }

    protected void errorJsonMsg(HttpServletResponse resp, HttpStatus httpStatus, String msgBody) throws IOException {
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resp.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        resp.getWriter().write(msgBody);
        resp.setStatus(httpStatus.value());
    }

    protected void errorJsonMsg(HttpServletResponse resp, HttpStatus httpStatus, BizErrEnum bizError) throws IOException {
        errorJsonMsg(resp, httpStatus, bizError, null);
    }

    protected void errorJsonMsg(HttpServletResponse resp, HttpStatus httpStatus, BizErrEnum bizError, Object body) throws IOException {
        errorJsonMsg(resp, httpStatus, JsonUtils.toJson(BaseResp.of(body, bizError)));
    }

    protected void errorJsonMsg(HttpServletResponse resp, HttpStatus httpStatus, BizErrEnum bizError, String msg, Object body) throws IOException {
        errorJsonMsg(resp, httpStatus, JsonUtils.toJson(BaseResp.of(body, bizError, msg)));
    }

    protected void errorJsonMsg(HttpServletResponse resp, HttpStatus httpStatus, String bizError, String msg) throws IOException {
        errorJsonMsg(resp, httpStatus, bizError, msg, null);
    }

    protected void errorJsonMsg(HttpServletResponse resp, HttpStatus httpStatus, String bizError, String msg, Object body) throws IOException {
        errorJsonMsg(resp, httpStatus, JsonUtils.toJson(BaseResp.of(body, bizError, msg)));
    }

}
