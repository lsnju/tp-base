package com.lsnju.base.http5.log;

import java.io.IOException;

import org.apache.hc.core5.http.EntityDetails;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.HttpResponseInterceptor;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lsnju.base.http.TpHttpConstants;

/**
 *
 * @author lis614
 * @since 2026-06-05 11:39
 * @version V1.0
 */
public class TpHttp5ResponseInterceptor implements HttpResponseInterceptor {

    private static final Logger TP_HTTP_HEADER = LoggerFactory.getLogger(TpHttpConstants.LOG_NAME);

    @Override
    public void process(HttpResponse response, EntityDetails entity, HttpContext context) throws HttpException, IOException {
        Object tpHttpId = context.removeAttribute(TpHttpConstants.REQ_ID);
        StringBuilder sb = new StringBuilder();
        sb.append("RESP_IN <<< ").append(tpHttpId).append(System.lineSeparator());
        sb.append("<<");
        sb.append(" ").append(response.getVersion());
        sb.append(" ").append(response.getCode());
        sb.append(" ").append(response.getReasonPhrase());
        sb.append(System.lineSeparator());
        for (Header header : response.getHeaders()) {
            sb.append("<< ")
                .append(header.getName())
                .append(": ")
                .append(header.getValue())
                .append(System.lineSeparator());
        }
        TP_HTTP_HEADER.info(sb.toString());
    }

}
