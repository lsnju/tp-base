package com.lsnju.tpbase.log;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

/**
 *
 * @author lis614
 * @since 2025/7/12 09:55
 * @version V1.0
 * @since v2.7.26
 */
public interface AopSkipMethod {

    Set<String> SKIP_METHOD = ImmutableSet.of("toString");

    default boolean skipDigest(String methodName) {
        return SKIP_METHOD.contains(methodName);
    }

    default boolean skipProfiler(String methodName) {
        return SKIP_METHOD.contains(methodName);
    }

}
