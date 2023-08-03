package com.lsnju.tpbase.log;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.lsnju.base.util.UUIDGenerator;
import com.lsnju.tpbase.web.filter.RequestId;


/**
 *
 * @author lisong
 * @since 2021/11/19 10:53
 * @version V1.0
 */
public class StreamingResponseBodyUtils {

    public static StreamingResponseBody wrap(StreamingResponseBody body) {
        final String reqId = MDC.get(RequestId.MDC_REQ_ID);
        return outputStream -> {
            String newId = StringUtils.join(StringUtils.substring(reqId, -16), LogRun.TAG, UUIDGenerator.getSUID());
            MDC.put(RequestId.MDC_REQ_ID, newId);
            try {
                body.writeTo(outputStream);
            } finally {
                MDC.put(RequestId.MDC_REQ_ID, reqId);
            }
        };
    }
}
