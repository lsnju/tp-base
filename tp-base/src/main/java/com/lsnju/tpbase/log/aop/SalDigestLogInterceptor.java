package com.lsnju.tpbase.log.aop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lsnju.tpbase.log.DigestConstants;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author ls
 * @since 2023-07-26 21:42:53
 * @version V1.0
 */
@Slf4j
public class SalDigestLogInterceptor extends AbstractDigestLogInterceptor {

    /** 摘要日志存储文件 */
    protected static final Logger digestLogger = LoggerFactory.getLogger(DigestConstants.TP_SAL_DIGEST);

    @Override
    Logger digestLogger() {
        return digestLogger;
    }

}
