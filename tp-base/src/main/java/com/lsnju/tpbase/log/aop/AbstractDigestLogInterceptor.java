package com.lsnju.tpbase.log.aop;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopProxyUtils;

import com.lsnju.base.util.Profiler;
import com.lsnju.tpbase.log.AopSkipMethod;
import com.lsnju.tpbase.log.DigestConstants;
import com.lsnju.tpbase.util.TpAopUtils;

/**
 *
 * @author lis614
 * @since 2024/12/12 14:00
 * @version V1.0
 */
public abstract class AbstractDigestLogInterceptor implements MethodInterceptor, DigestConstants, AopSkipMethod {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    public abstract Logger digestLogger();

    // [接口.方法名,耗时,成功失败]
    /** 日志格式 */
    protected static final String FORMAT_STR = "[%s.%s,%sms,%s]";


    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        log.debug("{}", invocation);

        Method method = invocation.getMethod();
        String className = getClassName(invocation);
        String methodName = method.getName();
        if (skipProfiler(methodName)) {
            return invocation.proceed();
        }

        Object ret = null;
        String code = "S";
        long startTime = System.nanoTime();
        try {
            String argDesc = TpAopUtils.argumentsDesc(invocation);
            if (StringUtils.isNotBlank(argDesc)) {
                Profiler.enter(String.format("%s.%s %s", className, methodName, argDesc));
            } else {
                Profiler.enter(String.format("%s.%s", className, methodName));
            }
            ret = invocation.proceed();
            return ret;
        } catch (Throwable e) {
            code = "E";
            throw e;
        } finally {
            Profiler.release(TpAopUtils.respDesc(ret));
            if (digestLogger().isInfoEnabled() && !skipDigest(methodName)) {
                digestLogger().info(String.format(FORMAT_STR, className, methodName, (System.nanoTime() - startTime) / MS_SCALE, code));
            }
        }
    }

    private String getClassName(MethodInvocation invocation) {
        Object target = invocation.getThis();
        if (target == null) {
            return invocation.getMethod().getDeclaringClass().getSimpleName();
        }

        if (log.isDebugEnabled()) {
            boolean proxyClass = Proxy.isProxyClass(target.getClass());
            log.debug("--------------------");
            log.debug("getThis = {}", target);
            log.debug("getThis.class = {}", target.getClass());
            log.debug("getThis.class is proxy = {}", proxyClass);
            log.debug("getThis.class.name = {}", target.getClass().getSimpleName());
            if (proxyClass) {
                Class<?>[] classes = AopProxyUtils.proxiedUserInterfaces(target);
                for (Class<?> c : classes) {
                    log.debug("__ c = {}", c);
                }
            }
            log.debug("---------------------");
        }

//        if (Proxy.isProxyClass(target.getClass())) {
//            return classes[0].getSimpleName();
//        }
        return invocation.getMethod().getDeclaringClass().getSimpleName();
    }

}
