package com.lsnju.base.http.log;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.RequestLine;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lsnju.base.util.UUIDGenerator;

/**
 *
 * @author lis614
 * @since 2026-06-05 11:40
 * @version V1.0
 */
public class TpHttpRequestInterceptor implements HttpRequestInterceptor {

    private static final Logger TP_HTTP_HEADER = LoggerFactory.getLogger("TP_HTTP_HEADER");

    @Override
    public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
        String tpHttpId = UUIDGenerator.getSUID();
        context.setAttribute("TP_HTTP_ID", tpHttpId);
        RequestLine requestLine = request.getRequestLine();
        StringBuilder sb = new StringBuilder();
        sb.append("REQ_OUT >>> ").append(tpHttpId).append(System.lineSeparator());
        sb.append(">>");
        sb.append(" ").append(requestLine.getMethod());
        sb.append(" ").append(requestLine.getUri());
        sb.append(" ").append(requestLine.getProtocolVersion());
        sb.append(System.lineSeparator());
        for (Header header : request.getAllHeaders()) {
            sb.append(">> ")
                .append(header.getName())
                .append(": ")
                .append(header.getValue())
                .append(System.lineSeparator());
        }
        TP_HTTP_HEADER.info(sb.toString());
    }

}
