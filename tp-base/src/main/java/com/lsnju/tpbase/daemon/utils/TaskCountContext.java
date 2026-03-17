package com.lsnju.tpbase.daemon.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author lisong
 * @since 2020-01-20 14:48:06
 * @version V1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TaskCountContext {

    private static final ThreadLocal<Long> TASK_SIZE = new ThreadLocal<>();
    private static final ThreadLocal<Long> TOTAL_SIZE = new ThreadLocal<>();

    public static long getTotalSize() {
        Long v = TOTAL_SIZE.get();
        return v == null ? 0L : v;
    }

    public static void setTotalSize(long total) {
        TOTAL_SIZE.set(total);
    }

    public static long getTaskSize() {
        Long v = TASK_SIZE.get();
        return v == null ? 0L : v;
    }

    public static void setTaskSize(long taskSize) {
        TASK_SIZE.set(taskSize);
    }

    public static void clean() {
        TASK_SIZE.remove();
        TOTAL_SIZE.remove();
    }
}
