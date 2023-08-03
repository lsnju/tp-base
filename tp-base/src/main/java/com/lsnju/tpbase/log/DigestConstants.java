package com.lsnju.tpbase.log;

/**
 *
 * @author ls
 * @since 2023-07-25 22:52:22
 * @version V1.0
 */
public interface DigestConstants {

    String DAL_DIGEST_INTERCEPTOR_NAME = "dalDigestLogInterceptor";
    String DAL_DIGEST_PROXY_NAME = "dalDigestLogProxy";
    String SAL_DIGEST_INTERCEPTOR_NAME = "salDigestLogInterceptor";
    String SAL_DIGEST_PROXY_NAME = "salDigestLogProxy";

    /** 纳秒转微妙，精度转换比例 */
    long US_SCALE = 1000L;

    /** 纳秒转好吗，精度转换比例 */
    long MS_SCALE = 1000_000L;

    String TP_BIZ_DAEMON_DIGEST = "TP_BIZ_DAEMON_DIGEST";
    String TP_DAL_DIGEST = "TP_DAL_DIGEST";
    String TP_SAL_DIGEST = "TP_SAL_DIGEST";
    String TP_PROFILER = "TP_PROFILER";
    String TP_PAGE_PERF = "TP_PAGE_PERF";
    String TP_HTTP_HEADER = "TP_HTTP_HEADER";

    String TP_CP_MO_DIGEST = "TP_CP_MO_DIGEST";
    String TP_TP_MO_DIGEST = "TP_TP_MO_DIGEST";
    String TP_TS_MO_DIGEST = "TP_TS_MO_DIGEST";

    String WEB_X = "TP_WEB_X";

}
