package com.lsnju.base.util;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author lis614
 * @since 2025/6/10 12:28
 * @version V1.0
 */
@Slf4j
public class ProfilerFunTest {

    @Test
    void test_001() throws InterruptedException {
        Profiler.start("start");
        {
            {
                Profiler.enter("entry_01");
                {
                    Profiler.enter("entry_01_1");
                    Thread.sleep(100);
                    Profiler.release(" -3");
                }
                {
                    Profiler.enter("entry_01_2");
                    Thread.sleep(100);
                    Profiler.release();
                }
                Profiler.release();
            }
            {
                Profiler.enter("entry_02");
                {
                    Profiler.enter("entry_02_1");
                    Thread.sleep(100);
                    Profiler.release(" -3");
                }
                {
                    Profiler.enter("entry_02_2");
                    Thread.sleep(100);
                    Profiler.release();
                }
                Profiler.release();
            }
        }
        Profiler.release();
        log.info("\n{}", Profiler.dump());
        log.info("\n{}", Profiler.dump(">>> "));
        log.info("\n{}", Profiler.dump("111 ", "222 "));
    }

}
