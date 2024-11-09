package com.lsnju.base.util;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author ls
 * @since 2024/11/9 14:12
 * @version V1.0
 */
@Slf4j
public class ClassLoaderTest {

    @Test
    void test_001() {
        ClassLoader classLoader = ClassLoaderTest.class.getClassLoader();
        while (classLoader != null) {
            log.info("{}", classLoader);
            log.info("{}", classLoader.equals(ClassLoader.getSystemClassLoader()));
            classLoader = classLoader.getParent();
        }
    }

}
