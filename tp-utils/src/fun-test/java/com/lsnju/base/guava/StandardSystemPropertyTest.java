package com.lsnju.base.guava;

import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemProperties;
import org.apache.commons.lang3.stream.Streams;
import org.junit.jupiter.api.Test;

import com.google.common.base.Splitter;
import com.google.common.base.StandardSystemProperty;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author ls
 * @since 2024/11/9 13:35
 * @version V1.0
 */
@Slf4j
public class StandardSystemPropertyTest {

    @Test
    void test_001() {
        final String separator = StandardSystemProperty.PATH_SEPARATOR.value();
        final String classpath = StandardSystemProperty.JAVA_CLASS_PATH.value();
        log.info("{}", separator);
        log.info("{}", classpath);

        log.info("------------------------------");
        Objects.requireNonNull(separator);
        Objects.requireNonNull(classpath);
        final Iterable<String> split = Splitter.on(separator).split(classpath);

        final List<String> list = Streams.of(split).toList();
        log.info("{}", list.size());

        for (String entry : list) {
            log.info("{}", entry);
        }
    }

    @Test
    void test_002() {
        final String separator = SystemProperties.getPathSeparator();
        final String classpath = SystemProperties.getJavaClassPath();
        log.info("{}", separator);
        log.info("{}", classpath);

        log.info("------------------------------");
        final String[] split = StringUtils.split(classpath, separator);

        final List<String> list = Streams.of(split).toList();
        log.info("{}", list.size());

        for (String entry : list) {
            log.info("{}", entry);
        }
    }
}
