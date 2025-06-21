package com.lsnju.tpbase.log.aop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lsnju.tpbase.log.DigestConstants;

/**
 * dao性能摘要日志拦截器
 *
 * @author ls
 * @since 2023-07-26 21:42:40
 * @version V1.0
 */
public class DalDigestLogInterceptor extends AbstractDigestLogInterceptor {

    /** 摘要日志存储文件 */
    private static final Logger digestLogger = LoggerFactory.getLogger(DigestConstants.TP_DAL_DIGEST);

    @Override
    public Logger digestLogger() {
        return digestLogger;
    }

}
