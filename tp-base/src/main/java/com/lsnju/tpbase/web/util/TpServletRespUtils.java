package com.lsnju.tpbase.web.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.lsnju.base.enums.BizErrEnum;
import com.lsnju.base.model.rs.BaseResp;
import com.lsnju.base.util.TpJsonUtils;

import jakarta.servlet.http.HttpServletResponse;


/**
 *
 * @author lisong
 * @since 2022/9/5 15:49
 * @version V1.0
 */
public class TpServletRespUtils {

    public static void errorJsonMsg(HttpServletResponse resp, HttpStatus httpStatus, String msgBody) throws IOException {
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resp.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        resp.getWriter().write(msgBody);
        resp.setStatus(httpStatus.value());
    }

    public static void errorJsonMsg(HttpServletResponse resp, HttpStatus httpStatus, BizErrEnum bizError) throws IOException {
        errorJsonMsg(resp, httpStatus, bizError, null);
    }

    public static void errorJsonMsg(HttpServletResponse resp, HttpStatus httpStatus, BizErrEnum bizError, Object body) throws IOException {
        errorJsonMsg(resp, httpStatus, TpJsonUtils.toJson(BaseResp.of(body, bizError)));
    }

    public static void errorJsonMsg(HttpServletResponse resp, HttpStatus httpStatus, BizErrEnum bizError, String msg, Object body) throws IOException {
        errorJsonMsg(resp, httpStatus, TpJsonUtils.toJson(BaseResp.of(body, bizError, msg)));
    }

    public static void errorJsonMsg(HttpServletResponse resp, HttpStatus httpStatus, String bizError, String msg) throws IOException {
        errorJsonMsg(resp, httpStatus, bizError, msg, null);
    }

    public static void errorJsonMsg(HttpServletResponse resp, HttpStatus httpStatus, String bizError, String msg, Object body) throws IOException {
        errorJsonMsg(resp, httpStatus, TpJsonUtils.toJson(BaseResp.of(body, bizError, msg)));
    }

}
