package com.lsnju.tpbase.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import com.lsnju.base.util.UUIDGenerator;
import com.lsnju.tpbase.web.filter.RequestId;

/**
 *
 * @author lis614
 * @since 2024/12/14 10:38
 * @version V1.0
 */
public class TpTraceUtils {

    public static final String TAG = ":";

    public static String currentTraceId() {
        return MDC.get(RequestId.MDC_REQ_ID);
    }

    public static String newTraceId() {
        return UUIDGenerator.getSUID();
    }

    public static String newTraceId(String traceId) {
        if (StringUtils.isBlank(traceId)) {
            return UUIDGenerator.getSUID();
        }
        return StringUtils.join(StringUtils.substring(traceId, -16), TAG, UUIDGenerator.getSUID());
    }

}
