package com.lsnju.tpbase.log.aspectj;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 *
 * @author ls
 * @since 2024/5/29 15:45
 * @version V1.0
 */
public interface ProceedingJoinPointInterceptor {
    Object proceed(ProceedingJoinPoint pjp) throws Throwable;
}
