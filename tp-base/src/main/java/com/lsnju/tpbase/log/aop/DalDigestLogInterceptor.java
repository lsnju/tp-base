package com.lsnju.tpbase.log.aop;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopProxyUtils;

import com.lsnju.base.util.Profiler;
import com.lsnju.tpbase.config.prop.TpAopConfigProperties;
import com.lsnju.tpbase.log.DigestConstants;

import lombok.extern.slf4j.Slf4j;

/**
 * dao性能摘要日志拦截器
 *
 * @author ls
 * @since 2023-07-26 21:42:40
 * @version V1.0
 */
@Slf4j
public class DalDigestLogInterceptor implements MethodInterceptor, DigestConstants {

    /** 摘要日志存储文件 */
    protected static final Logger digestLogger = LoggerFactory.getLogger(DigestConstants.TP_DAL_DIGEST);

    // [接口.方法名,耗时,成功失败]
    /** 日志格式 */
    protected static final String FORMAT_STR = "[%s.%s,%sms,%s]";

    private final TpAopConfigProperties tpAopConfigProperties;

    public DalDigestLogInterceptor(TpAopConfigProperties tpAopConfigProperties) {
        this.tpAopConfigProperties = tpAopConfigProperties;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        log.debug("{}", invocation);

        Method method = invocation.getMethod();
        String className = method.getDeclaringClass().getSimpleName();
        String methodName = method.getName();

        String code = "S";
        long startTime = System.nanoTime();
        try {
            Profiler.enter(String.format("%s.%s", className, methodName));
            return invocation.proceed();
        } catch (Throwable e) {
            code = "E";
            throw e;
        } finally {
            Profiler.release();
            if (digestLogger.isInfoEnabled()) {
                digestLogger.info(String.format(FORMAT_STR, className, methodName, (System.nanoTime() - startTime) / MS_SCALE, code));
            }
        }
    }

    private String getClassName(MethodInvocation invocation) {
        if (tpAopConfigProperties.isUseSpring() && invocation.getThis() != null) {
            Class<?>[] classes = AopProxyUtils.proxiedUserInterfaces(invocation.getThis());
            return classes[0].getSimpleName();
        } else {
            return invocation.getMethod().getDeclaringClass().getSimpleName();
        }
    }

}
