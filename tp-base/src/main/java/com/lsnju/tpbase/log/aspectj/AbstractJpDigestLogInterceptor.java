package com.lsnju.tpbase.log.aspectj;

import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopProxyUtils;

import com.lsnju.base.util.Profiler;
import com.lsnju.tpbase.log.DigestConstants;
import com.lsnju.tpbase.util.TpAopUtils;

/**
 *
 * @author ls
 * @since 2024/11/15 22:27
 * @version V1.0
 */
public abstract class AbstractJpDigestLogInterceptor implements DigestConstants, ProceedingJoinPointInterceptor {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    // [接口.方法名,耗时,成功失败]
    /** 日志格式 */
    protected static final String FORMAT_STR = "[%s.%s,%sms,%s]";

    public abstract Logger digestLogger();

    @Override
    public Object proceed(ProceedingJoinPoint pjp) throws Throwable {
        log.debug("{}", pjp);

        String className = getInterfaceName(pjp);
        String methodName = pjp.getSignature().getName();

        String code = "S";
        long startTime = System.nanoTime();
        try {
            String argDesc = argumentsDesc(pjp.getArgs());
            if (StringUtils.isNotBlank(argDesc)) {
                Profiler.enter(String.format("[AJ] %s.%s %s", className, methodName, argDesc));
            } else {
                Profiler.enter(String.format("[AJ] %s.%s", className, methodName));
            }
            return pjp.proceed();
        } catch (Throwable e) {
            code = "E";
            throw e;
        } finally {
            Profiler.release();
            if (digestLogger().isInfoEnabled()) {
                digestLogger().info(String.format(FORMAT_STR, className, methodName, (System.nanoTime() - startTime) / MS_SCALE, code));
            }
        }

    }

    public String argumentsDesc(Object[] arguments) {
        if (arguments.length == 0) {
            return "()";
        }
        String value = Arrays.stream(arguments).map(TpAopUtils::argDesc).collect(Collectors.joining(", "));
        return "(" + value + ")";
    }

    private String getInterfaceName(ProceedingJoinPoint pjp) {
        boolean isProxyClass = Proxy.isProxyClass(pjp.getTarget().getClass());
        if (log.isDebugEnabled()) {
            log.debug("getThis = {}", pjp.getThis());
            log.debug("getTarget = {}", pjp.getTarget());
            log.debug("getTarget.class = {}", pjp.getTarget().getClass());
            log.debug("getTarget.class.name = {}", pjp.getTarget().getClass().getSimpleName());
            log.debug("getTarget is proxy = {}", isProxyClass);
        }
        if (isProxyClass) {
            Class<?>[] classes = AopProxyUtils.proxiedUserInterfaces(pjp.getTarget());
            if (log.isDebugEnabled()) {
                for (Class<?> c : classes) {
                    log.debug("__ c = {}", c);
                }
            }
            return classes[0].getSimpleName();
        }
        return pjp.getTarget().getClass().getSimpleName();
    }

}
