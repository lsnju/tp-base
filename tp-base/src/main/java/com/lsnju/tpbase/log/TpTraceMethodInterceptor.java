package com.lsnju.tpbase.log;

import java.lang.reflect.Method;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 *
 * @author lisong
 * @since 2025/1/2 09:12
 * @version V1.0
 */
public class TpTraceMethodInterceptor implements MethodInterceptor {

    private final TpTraceInterceptor interceptor;

    public TpTraceMethodInterceptor() {
        this.interceptor = new TpTraceInterceptor();
    }

    public TpTraceMethodInterceptor(TpTraceInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    public TpTraceMethodInterceptor(String prefix, String logName) {
        this.interceptor = new TpTraceInterceptor(prefix, logName);
    }

    @Nullable
    @Override
    public Object invoke(@Nonnull MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        String className = method.getDeclaringClass().getSimpleName();
        String methodName = method.getName();
        return interceptor.call(String.format("%s.%s", className, methodName), () -> {
            try {
                return invocation.proceed();
            } catch (Throwable e) {
                throw new Exception(e);
            }
        });
    }

}
