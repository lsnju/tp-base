package com.lsnju.tpbase.log;

import org.slf4j.MDC;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.lsnju.tpbase.util.TpTraceUtils;
import com.lsnju.tpbase.web.filter.RequestId;


/**
 *
 * @author lisong
 * @since 2021/11/19 10:53
 * @version V1.0
 */
public class StreamingResponseBodyUtils {

    public static StreamingResponseBody wrap(StreamingResponseBody body) {
        final String currentReqId = TpTraceUtils.currentTraceId();
        final String newId = TpTraceUtils.newTraceId(currentReqId);
        return outputStream -> {
            try {
                MDC.put(RequestId.MDC_REQ_ID, newId);
                body.writeTo(outputStream);
            } finally {
                MDC.put(RequestId.MDC_REQ_ID, currentReqId);
            }
        };
    }

}
