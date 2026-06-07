package com.lsnju.base.http5.log;

import java.io.IOException;

import org.apache.hc.core5.http.EntityDetails;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.HttpRequest;
import org.apache.hc.core5.http.HttpRequestInterceptor;
import org.apache.hc.core5.http.HttpVersion;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lsnju.base.http.TpHttpConstants;
import com.lsnju.base.util.UUIDGenerator;

/**
 *
 * @author lis614
 * @since 2026-06-05 11:40
 * @version V1.0
 */
public class TpHttp5RequestInterceptor implements HttpRequestInterceptor {

    private static final Logger TP_HTTP_HEADER = LoggerFactory.getLogger(TpHttpConstants.LOG_NAME);

    @Override
    public void process(HttpRequest request, EntityDetails entity, HttpContext context) throws HttpException, IOException {
        if (!TP_HTTP_HEADER.isInfoEnabled()) {
            return;
        }
        String tpHttpId = UUIDGenerator.getSUID();
        context.setAttribute(TpHttpConstants.REQ_ID, tpHttpId);
        StringBuilder sb = new StringBuilder();
        sb.append("REQ_OUT >>> ").append(tpHttpId).append(System.lineSeparator());
        sb.append(">>");
        sb.append(" ").append(request.getMethod());
        sb.append(" ").append(request.getRequestUri());
        if (request.getVersion() != null) {
            sb.append(" ").append(request.getVersion());
        } else {
            sb.append(" ").append(HttpVersion.DEFAULT);
        }
        sb.append(System.lineSeparator());
        for (Header header : request.getHeaders()) {
            sb.append(">> ")
                .append(header.getName())
                .append(": ")
                .append(header.getValue())
                .append(System.lineSeparator());
        }
        TP_HTTP_HEADER.info(sb.toString());
    }

}
