package com.lsnju.tpbase.test;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.RandomUtils;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import com.lsnju.tpbase.log.TpTraceInterceptor;
import com.lsnju.tpbase.test.vo.PerfResult;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author lisong
 * @since 2025/11/15 13:42
 * @version V1.0
 */
@Slf4j
public class TpPerformanceUtilsFunTest {

    @Test
    void test_001() {
        TpTraceInterceptor.DEFAULT.run("x", () -> {
            try {
                test001();
            } catch (Exception e) {
                log.error(String.format("%s", e.getMessage()), e);
            }
        });
    }

    private static void test001() throws Exception {
        List<List<String>> allTask = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            allTask.add(Lists.newArrayList("a", "b", "c"));
        }
        PerfResult result = TpPerformanceUtils.perfTest(allTask, s -> {
            //
            try {
                log.info("{}", s);
                Thread.sleep(RandomUtils.insecure().randomInt(0, 1000));
            } catch (InterruptedException e) {
                log.error(String.format("%s", e.getMessage()), e);
            }
        });
        log.info("{}, {}, avg={}", result.getTotalCost(), result.getTotalCount(), result.avg());
        for (PerfResult item : result.getSubList()) {
            log.info("{}, avg={}", item, item.avg());
        }
    }

}
