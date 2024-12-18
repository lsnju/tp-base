package com.lsnju.tpbase.log.aspectj;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lsnju.tpbase.log.DigestConstants;

/**
 *
 * @author ls
 * @since 2024/5/28 12:52
 * @version V1.0
 */
public class TpSalJpDigestLogInterceptor extends AbstractJpDigestLogInterceptor {

    /** 摘要日志存储文件 */
    protected static final Logger digestLogger = LoggerFactory.getLogger(DigestConstants.TP_SAL_DIGEST);

    @Override
    Logger digestLogger() {
        return digestLogger;
    }

}
