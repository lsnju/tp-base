package com.lsnju.base.http.log;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.StatusLine;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author lis614
 * @since 2026-06-05 11:39
 * @version V1.0
 */
public class TpHttpResponseInterceptor implements HttpResponseInterceptor {

    private static final Logger TP_HTTP_HEADER = LoggerFactory.getLogger("TP_HTTP_HEADER");

    @Override
    public void process(HttpResponse response, HttpContext context) throws HttpException, IOException {
        Object tpHttpId = context.getAttribute("TP_HTTP_ID");
        StatusLine statusLine = response.getStatusLine();
        StringBuilder sb = new StringBuilder();
        sb.append("RESP_IN <<< ").append(tpHttpId).append(System.lineSeparator());
        sb.append("<<");
        sb.append(" ").append(statusLine.getProtocolVersion());
        sb.append(" ").append(statusLine.getStatusCode());
        sb.append(" ").append(statusLine.getReasonPhrase());
        sb.append(System.lineSeparator());
        for (Header header : response.getAllHeaders()) {
            sb.append("<< ")
                .append(header.getName())
                .append(": ")
                .append(header.getValue())
                .append(System.lineSeparator());
        }
        TP_HTTP_HEADER.info(sb.toString());
    }

}
