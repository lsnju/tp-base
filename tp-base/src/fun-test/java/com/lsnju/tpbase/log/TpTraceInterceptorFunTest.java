package com.lsnju.tpbase.log;

import org.junit.jupiter.api.Test;
import org.slf4j.MDC;

import com.lsnju.base.util.Profiler;
import com.lsnju.tpbase.util.TpTraceUtils;
import com.lsnju.tpbase.web.filter.RequestId;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author lis614
 * @since 2025/8/19 20:12
 * @version V1.0
 */
@Slf4j
public class TpTraceInterceptorFunTest {

    @Test
    void test_runnable() {
        MDC.put(RequestId.MDC_REQ_ID, TpTraceUtils.newTraceId());
        log.info("test_runnable");
        TpTraceInterceptor.DEFAULT.runnable("xx", () -> {
            String ret = testMethod();
            log.info("testMethod.runnable.ret = {}", ret);
        }).run();
    }

    @Test
    void test_callable() throws Exception {
        MDC.put(RequestId.MDC_REQ_ID, TpTraceUtils.newTraceId());
        log.info("test_callable");
        String result = TpTraceInterceptor.DEFAULT.callable("xx", () -> {
            String ret = testMethod();
            log.info("testMethod.callable.ret = {}", ret);
            return ret;
        }).call();
        log.info("{}", result);
    }

    private String testMethod() {
        log.info("testMethod");
        {
            Profiler.enter("hello_1");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                log.error(String.format("%s", e.getMessage()), e);
            }
            Profiler.release();
        }
        {
            Profiler.enter("hello_2");
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                log.error(String.format("%s", e.getMessage()), e);
            }
            Profiler.release();
        }
        return "ok";
    }
}
