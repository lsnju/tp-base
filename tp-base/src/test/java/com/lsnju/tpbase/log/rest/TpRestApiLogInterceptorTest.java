package com.lsnju.tpbase.log.rest;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.lsnju.tpbase.config.prop.TpAopConfigProperties;
import com.lsnju.tpbase.log.annotation.TpSkipLog;

class TpRestApiLogInterceptorTest {

    @Test
    void logRequest_skipAnnotatedParameter() throws NoSuchMethodException {
        TpAopConfigProperties config = new TpAopConfigProperties();
        TpRestApiLogInterceptor interceptor = new TpRestApiLogInterceptor(config, Object::toString);

        ProceedingJoinPoint joinPoint = Mockito.mock(ProceedingJoinPoint.class);
        MethodSignature signature = Mockito.mock(MethodSignature.class);

        Method method = DemoApi.class.getDeclaredMethod("demoMethod", String.class, String.class, Integer.class);
        Mockito.when(signature.getMethod()).thenReturn(method);
        Mockito.when(signature.toString()).thenReturn("DemoApi.demoMethod(..)");
        Mockito.when(joinPoint.getSignature()).thenReturn(signature);
        Mockito.when(joinPoint.getArgs()).thenReturn(new Object[]{"A", "SECRET", 123});

        String reqStr = interceptor.logRequest(joinPoint);

        Assertions.assertTrue(reqStr.contains("arg0=A"));
        Assertions.assertTrue(reqStr.contains("arg2=123"));
        Assertions.assertFalse(reqStr.contains("arg1=SECRET"));
    }

    @Test
    void logRequest_logAllParametersWhenNoAnnotation() throws NoSuchMethodException {
        TpAopConfigProperties config = new TpAopConfigProperties();
        TpRestApiLogInterceptor interceptor = new TpRestApiLogInterceptor(config, Object::toString);

        ProceedingJoinPoint joinPoint = Mockito.mock(ProceedingJoinPoint.class);
        MethodSignature signature = Mockito.mock(MethodSignature.class);

        Method method = DemoApi.class.getDeclaredMethod("demoMethodWithoutSkip", String.class, String.class, Integer.class);
        Mockito.when(signature.getMethod()).thenReturn(method);
        Mockito.when(signature.toString()).thenReturn("DemoApi.demoMethodWithoutSkip(..)");
        Mockito.when(joinPoint.getSignature()).thenReturn(signature);
        Mockito.when(joinPoint.getArgs()).thenReturn(new Object[]{"A", "VISIBLE", 123});

        String reqStr = interceptor.logRequest(joinPoint);

        Assertions.assertTrue(reqStr.contains("arg0=A"));
        Assertions.assertTrue(reqStr.contains("arg1=VISIBLE"));
        Assertions.assertTrue(reqStr.contains("arg2=123"));
    }

    static class DemoApi {
        @SuppressWarnings("unused")
        void demoMethod(String visible, @TpSkipLog String hidden, Integer count) {
        }

        @SuppressWarnings("unused")
        void demoMethodWithoutSkip(String arg0, String arg1, Integer arg2) {
        }
    }
}
