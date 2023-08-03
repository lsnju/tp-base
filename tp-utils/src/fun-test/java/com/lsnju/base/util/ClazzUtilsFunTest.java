package com.lsnju.base.util;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author ls
 * @since 2023/7/20 20:18
 * @version V1.0
 */
@Slf4j
public class ClazzUtilsFunTest {

    @Test
    public void test_001() {
        log.info("{}", ClazzUtils.getCodeLocation(Test.class));
        log.info("{}", ClazzUtils.getImplementationVersion(Test.class));
        log.info("{}", ClazzUtils.getImplementationVersion(Test.class));

        log.info("{}", ClazzUtils.getParentDirectoryFromJar(ClazzUtilsFunTest.class));
        log.info("{}", ClazzUtils.getParentDirectoryFromJar());
        log.info("{}", ClazzUtils.getParentDirectoryFromJar(Test.class));

        log.info("{}", ClazzUtils.getWorkingDir(ClazzUtilsFunTest.class));
        log.info("{}", ClazzUtils.getWorkingDir());
        log.info("{}", ClazzUtils.getWorkingDir(Test.class));
    }

}
