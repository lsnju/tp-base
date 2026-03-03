package com.lsnju.tpbase.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.util.AntPathMatcher;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author lis614
 * @since 2026-03-01 14:06
 * @version V1.0
 */
@Slf4j
public class AntPathMatcherFunTest {

    @Test
    void test_001() {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        Assertions.assertTrue(antPathMatcher.match("/rest/**", "/rest/a"));
        Assertions.assertTrue(antPathMatcher.match("/rest/**", "/rest/a/b"));
        Assertions.assertTrue(antPathMatcher.match("/rest/**", "/rest/a/b/c"));
    }

}
